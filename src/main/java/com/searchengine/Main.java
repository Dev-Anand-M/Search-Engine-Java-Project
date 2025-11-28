package com.searchengine;

import com.searchengine.scheduler.CrawlerScheduler;
import com.searchengine.util.DatabaseSetup;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {
    public static void main(String[] args) throws LifecycleException {
        System.out.println("=== Java Search Engine Starting ===");
        
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "8080";
        }

        // Setup database tables
        System.out.println("Setting up database...");
        DatabaseSetup.setupDatabase();

        // Start scheduled crawler (runs every 24 hours)
        String enableCrawler = System.getenv("ENABLE_CRAWLER");
        if ("true".equalsIgnoreCase(enableCrawler)) {
            System.out.println("✓ Scheduled crawler enabled (runs every 24 hours)");
            CrawlerScheduler.startScheduledCrawling();
        } else {
            System.out.println("ℹ Scheduled crawler disabled (set ENABLE_CRAWLER=true to enable)");
        }

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(Integer.parseInt(port));
        tomcat.getConnector();

        String webappDir = new File("src/main/webapp").getAbsolutePath();
        Context context = tomcat.addWebapp("", webappDir);
        
        System.out.println("✓ Server starting on port " + port);
        System.out.println("===================================");
        tomcat.start();
        tomcat.getServer().await();
    }
}
