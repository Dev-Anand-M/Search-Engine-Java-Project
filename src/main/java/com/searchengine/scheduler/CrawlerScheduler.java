package com.searchengine.scheduler;

import com.searchengine.crawler.WebCrawler;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CrawlerScheduler {
    private static final long CRAWL_INTERVAL = 24 * 60 * 60 * 1000; // 24 hours
    private static Timer timer;

    public static void startScheduledCrawling() {
        timer = new Timer(true); // Daemon thread
        
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("=== Starting Scheduled Crawl ===");
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
                System.out.println("=== Scheduled Crawl Completed ===");
            }
        }, 0, CRAWL_INTERVAL); // Start immediately, then every 24 hours
    }

    public static void stopScheduledCrawling() {
        if (timer != null) {
            timer.cancel();
        }
    }
}
