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
    private static final int MAX_PAGES = 1000; // Crawl up to 1000 pages
    private static final int DELAY_MS = 2000; // 2 seconds between requests
    private static final int MAX_DEPTH = 3; // Maximum link depth
    private Map<String, Integer> urlDepth = new HashMap<>();
    
    private static final Set<String> BASE_URLS = new HashSet<>(Arrays.asList(
            "https://en.wikipedia.org",
            "https://www.gutenberg.org",
            "https://archive.org",
            "https://openlibrary.org",
            "https://plato.stanford.edu"
    ));

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        List<String> seeds = Arrays.asList(
                "https://en.wikipedia.org/wiki/Main_Page",
                "https://en.wikipedia.org/wiki/Science",
                "https://en.wikipedia.org/wiki/Technology",
                "https://en.wikipedia.org/wiki/History",
                "https://www.gutenberg.org/",
                "https://archive.org/",
                "https://openlibrary.org/",
                "https://plato.stanford.edu/",
                "https://www.britannica.com/",
                "https://www.bbc.com/news/technology",
                "https://www.reuters.com/technology/",
                "https://www.nature.com/",
                "https://www.sciencedaily.com/"
        );
        crawler.startCrawling(seeds);
    }

    public void startCrawling(List<String> startUrls) {
        // Initialize seed URLs with depth 0
        for (String url : startUrls) {
            urlsToVisit.offer(url);
            urlDepth.put(url, 0);
        }
        
        int pagesCrawled = 0;
        int errorCount = 0;
        long startTime = System.currentTimeMillis();
        
        System.out.println("=== Starting Web Crawler ===");
        System.out.println("Max pages: " + MAX_PAGES);
        System.out.println("Delay between requests: " + DELAY_MS + "ms");
        System.out.println("Max depth: " + MAX_DEPTH);
        System.out.println("Seed URLs: " + startUrls.size());
        System.out.println("============================\n");
        
        while (!urlsToVisit.isEmpty() && pagesCrawled < MAX_PAGES) {
            String currentUrl = urlsToVisit.poll();
            int currentDepth = urlDepth.getOrDefault(currentUrl, 0);

            if (visitedUrls.contains(currentUrl) || !isValidUrl(currentUrl) || currentDepth > MAX_DEPTH) {
                continue;
            }

            try {
                System.out.printf("[%d/%d] Crawling (depth %d): %s\n", 
                    pagesCrawled + 1, MAX_PAGES, currentDepth, currentUrl);

                Document document = Jsoup.connect(currentUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                        .timeout(10000)
                        .followRedirects(true)
                        .ignoreHttpErrors(true)
                        .get();

                visitedUrls.add(currentUrl);

                String title = document.title();
                String content = document.body().text();

                // Only save pages with meaningful content
                if (content.length() > 100 && title.length() > 0) {
                    WebPage page = new WebPage(currentUrl, title, content);
                    savePage(page);
                    pagesCrawled++;
                    
                    // Extract links only if we haven't reached max depth
                    if (currentDepth < MAX_DEPTH) {
                        Elements links = document.select("a[href]");
                        int linksAdded = 0;
                        
                        for (Element link : links) {
                            String nextUrl = link.absUrl("href");
                            if (isValidUrl(nextUrl) && !visitedUrls.contains(nextUrl) 
                                && !urlDepth.containsKey(nextUrl)) {
                                urlsToVisit.offer(nextUrl);
                                urlDepth.put(nextUrl, currentDepth + 1);
                                linksAdded++;
                                
                                // Limit links per page to avoid queue explosion
                                if (linksAdded >= 20) break;
                            }
                        }
                        System.out.println("  → Added " + linksAdded + " new links to queue");
                    }
                    
                    // Progress update every 50 pages
                    if (pagesCrawled % 50 == 0) {
                        long elapsed = (System.currentTimeMillis() - startTime) / 1000;
                        System.out.println("\n--- Progress Update ---");
                        System.out.println("Pages crawled: " + pagesCrawled);
                        System.out.println("Queue size: " + urlsToVisit.size());
                        System.out.println("Time elapsed: " + elapsed + "s");
                        System.out.println("Errors: " + errorCount);
                        System.out.println("----------------------\n");
                    }
                }

                // Rate limiting - be polite to servers
                Thread.sleep(DELAY_MS);

            } catch (Exception e) {
                errorCount++;
                System.err.println("  ✗ Error: " + e.getMessage());
                
                // If too many errors, slow down
                if (errorCount % 10 == 0) {
                    try {
                        System.out.println("  ⚠ Too many errors, pausing for 10 seconds...");
                        Thread.sleep(10000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        
        long totalTime = (System.currentTimeMillis() - startTime) / 1000;
        System.out.println("\n=== Crawling Completed ===");
        System.out.println("Total pages crawled: " + pagesCrawled);
        System.out.println("Total time: " + totalTime + "s");
        System.out.println("Average: " + (pagesCrawled > 0 ? totalTime / pagesCrawled : 0) + "s per page");
        System.out.println("Total errors: " + errorCount);
        System.out.println("==========================");
    }

    private boolean isValidUrl(String url) {
        if (url == null || url.isEmpty()) return false;
        
        // Skip fragments, files, and media
        if (url.contains("#") || url.contains("?action=") || 
            url.endsWith(".pdf") || url.endsWith(".jpg") || 
            url.endsWith(".png") || url.endsWith(".gif") ||
            url.endsWith(".mp4") || url.endsWith(".zip") ||
            url.endsWith(".xml") || url.endsWith(".json")) {
            return false;
        }
        
        // Skip special pages
        if (url.contains("Special:") || url.contains("File:") || 
            url.contains("Talk:") || url.contains("User:") ||
            url.contains("/login") || url.contains("/signup")) {
            return false;
        }
        
        // Check if URL starts with any allowed base
        for (String base : BASE_URLS) {
            if (url.startsWith(base)) return true;
        }
        return false;
    }

    private void savePage(WebPage page) {
        String sql = "INSERT INTO pages (url, title, content) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE title=?, content=?";

        try {
            Connection conn = DatabaseConnection.getInstance().getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, page.getUrl());
            stmt.setString(2, page.getTitle());
            stmt.setString(3, page.getContent());
            stmt.setString(4, page.getTitle());
            stmt.setString(5, page.getContent());

            stmt.executeUpdate();
            stmt.close(); // Only close the statement, not the connection

        } catch (SQLException e) {
            System.err.println("Error saving page: " + e.getMessage());
        }
    }
}
