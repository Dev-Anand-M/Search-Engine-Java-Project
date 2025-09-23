package com.searchengine.search;

import com.searchengine.database.DatabaseConnection;
import com.searchengine.model.WebPage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SearchEngine {

    public List<WebPage> search(String query, int limit) {
        List<WebPage> results = new ArrayList<>();

        if (query == null || query.trim().isEmpty()) {
            return results;
        }

        String[] keywords = query.toLowerCase()
                .replaceAll("[^a-zA-Z0-9\\s]", "")
                .split("\\s+");

        // Search in title and content
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("SELECT id, url, title, content FROM pages WHERE ");

        List<String> conditions = new ArrayList<>();
        for (int i = 0; i < keywords.length; i++) {
            conditions.add("(title LIKE ? OR content LIKE ?)");
        }

        sqlBuilder.append(String.join(" OR ", conditions));
        sqlBuilder.append(" LIMIT ?");

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlBuilder.toString())) {

            int paramIndex = 1;
            for (String keyword : keywords) {
                stmt.setString(paramIndex++, "%" + keyword + "%");
                stmt.setString(paramIndex++, "%" + keyword + "%");
            }
            stmt.setInt(paramIndex, limit);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    WebPage page = new WebPage();
                    page.setId(rs.getInt("id"));
                    page.setUrl(rs.getString("url"));
                    page.setTitle(rs.getString("title"));
                    page.setContent(rs.getString("content"));

                    // Calculate relevance score
                    int score = calculateRelevance(page.getTitle(), page.getContent(), query);
                    page.setRelevanceScore(score);

                    results.add(page);
                }
            }

        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }

        // Sort by relevance
        results.sort((a, b) -> Integer.compare(b.getRelevanceScore(), a.getRelevanceScore()));

        return results;
    }

    private int calculateRelevance(String title, String content, String query) {
        int score = 0;
        String[] keywords = query.toLowerCase().split("\\s+");

        for (String keyword : keywords) {
            // Title matches count more
            score += (title.toLowerCase().split(keyword, -1).length - 1) * 3;
            // Content matches
            score += (content.toLowerCase().split(keyword, -1).length - 1);
        }

        return score;
    }
}