package com.searchengine;

import com.searchengine.scheduler.CrawlerScheduler;
import com.searchengine.util.DatabaseSetup;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

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

        // Set base directory for Tomcat
        String baseDir = System.getProperty("java.io.tmpdir") + "/tomcat-work";
        tomcat.setBaseDir(baseDir);

        // Determine webapp directory
        String webappDir;
        File localWebapp = new File("src/main/webapp");
        
        if (localWebapp.exists()) {
            // Running locally
            webappDir = localWebapp.getAbsolutePath();
            System.out.println("Using local webapp directory: " + webappDir);
        } else {
            // Running from JAR - extract resources
            webappDir = baseDir + "/webapp";
            File webappFile = new File(webappDir);
            webappFile.mkdirs();
            
            // Copy webapp resources from JAR
            try {
                java.net.URL webappResource = Main.class.getClassLoader().getResource("webapp");
                if (webappResource != null) {
                    java.nio.file.Path source = java.nio.file.Paths.get(webappResource.toURI());
                    java.nio.file.Path target = java.nio.file.Paths.get(webappDir);
                    copyDirectory(source, target);
                    System.out.println("Extracted webapp resources to: " + webappDir);
                } else {
                    System.out.println("Warning: webapp resources not found in JAR");
                }
            } catch (Exception e) {
                System.err.println("Error extracting webapp: " + e.getMessage());
            }
        }
        
        Context context = tomcat.addWebapp("", webappDir);
        
        System.out.println("✓ Server starting on port " + port);
        System.out.println("===================================");
        tomcat.start();
        tomcat.getServer().await();
    }

    private static void copyDirectory(Path source, Path target) throws IOException {
        Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                Path targetDir = target.resolve(source.relativize(dir));
                Files.createDirectories(targetDir);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.copy(file, target.resolve(source.relativize(file)), StandardCopyOption.REPLACE_EXISTING);
                return FileVisitResult.CONTINUE;
            }
        });
    }
}
