package com.searchengine.crawler;

import com.searchengine.database.DatabaseConnection;
import com.searchengine.model.WebPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class WebCrawler {
    private Set<String> visitedUrls = new HashSet<>();
    private Queue<String> urlsToVisit = new LinkedList<>();
    private static final int MAX_PAGES = 10;
    private static final Set<String> BASE_URLS = new HashSet<>(Arrays.asList(
            "https://www.gutenberg.org",
            "https://archive.org",
            "https://commoncrawl.org",
            "https://data.gov",
            "https://openlibrary.org",
            "https://plato.stanford.edu",
            "https://en.wikipedia.org"
    ));

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        List<String> seeds = Arrays.asList(
                "https://www.gutenberg.org/",
                "https://archive.org/",
                "https://openlibrary.org/",
                "https://en.wikipedia.org/wiki/Main_Page"
        );
        crawler.startCrawling(seeds);
    }

    public void startCrawling(List<String> startUrls) {
        for (String url : startUrls) {
            urlsToVisit.offer(url);
        }
        int pagesCrawled = 0;
        while (!urlsToVisit.isEmpty() && pagesCrawled < MAX_PAGES) {
            String currentUrl = urlsToVisit.poll();

            if (visitedUrls.contains(currentUrl) || !isValidUrl(currentUrl)) {
                continue;
            }

            try {
                System.out.println("Crawling: " + currentUrl);

                Document document = Jsoup.connect(currentUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(5000)
                        .get();

                visitedUrls.add(currentUrl);

                String title = document.title();
                String content = document.body().text();

                WebPage page = new WebPage(currentUrl, title, content);
                savePage(page);

                // Extract links
                Elements links = document.select("a[href]");
                for (Element link : links) {
                    String nextUrl = link.absUrl("href");
                    if (isValidUrl(nextUrl) && !visitedUrls.contains(nextUrl)) {
                        urlsToVisit.offer(nextUrl);
                    }
                }

                pagesCrawled++;
                Thread.sleep(1000); // Be polite

            } catch (Exception e) {
                System.err.println("Error crawling " + currentUrl + ": " + e.getMessage());
            }
        }
        System.out.println("Crawling completed. Total pages: " + pagesCrawled);
    }

    private boolean isValidUrl(String url) {
        if (url == null || url.contains("#") || url.endsWith(".pdf") ||
                url.endsWith(".jpg") || url.endsWith(".png"))
            return false;
        for (String base : BASE_URLS) {
            if (url.startsWith(base)) return true;
        }
        return false;
    }

    private void savePage(WebPage page) {
        String sql = "INSERT INTO pages (url, title, content) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE title=?, content=?";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, page.getUrl());
            stmt.setString(2, page.getTitle());
            stmt.setString(3, page.getContent());
            stmt.setString(4, page.getTitle());
            stmt.setString(5, page.getContent());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("Error saving page: " + e.getMessage());
        }
    }
}
