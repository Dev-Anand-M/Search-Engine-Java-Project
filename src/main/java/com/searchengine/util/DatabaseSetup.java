package com.searchengine.util;

import com.searchengine.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DatabaseSetup {

    public static void main(String[] args) {
        setupDatabase();
    }

    public static void setupDatabase() {
        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            // Create pages table
            String createPagesTable = "CREATE TABLE IF NOT EXISTS pages (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "url VARCHAR(500) UNIQUE NOT NULL, " +
                    "title VARCHAR(500), " +
                    "content LONGTEXT, " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "INDEX idx_url (url), " +
                    "FULLTEXT(title, content)" +
                    ")";
            stmt.execute(createPagesTable);

            System.out.println("Database setup completed successfully!");

        } catch (SQLException e) {
            System.err.println("Database setup error: " + e.getMessage());
        }
    }
}