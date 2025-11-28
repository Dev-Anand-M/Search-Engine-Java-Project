package com.searchengine.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Debug: Print all environment variables starting with MYSQL
            System.out.println("=== Database Environment Variables ===");
            System.getenv().entrySet().stream()
                .filter(e -> e.getKey().contains("MYSQL") || e.getKey().contains("DB"))
                .forEach(e -> System.out.println(e.getKey() + " = " + 
                    (e.getKey().contains("PASSWORD") ? "***" : e.getValue())));
            System.out.println("=====================================");
            
            // Try multiple variable name patterns
            String host = System.getenv("MYSQLHOST");
            if (host == null) host = System.getenv("MYSQL_HOST");
            if (host == null) host = System.getenv("DB_HOST");
            
            String port = System.getenv("MYSQLPORT");
            if (port == null) port = System.getenv("MYSQL_PORT");
            if (port == null) port = System.getenv("DB_PORT");
            
            String database = System.getenv("MYSQLDATABASE");
            if (database == null) database = System.getenv("MYSQL_DATABASE");
            if (database == null) database = System.getenv("DB_NAME");
            
            String username = System.getenv("MYSQLUSER");
            if (username == null) username = System.getenv("MYSQL_USER");
            if (username == null) username = System.getenv("DB_USER");
            
            String password = System.getenv("MYSQLPASSWORD");
            if (password == null) password = System.getenv("MYSQL_PASSWORD");
            if (password == null) password = System.getenv("DB_PASSWORD");
            
            // Final fallback to local
            if (host == null) host = "localhost";
            if (port == null) port = "3306";
            if (database == null) database = "searchengine_db";
            if (username == null) username = "root";
            if (password == null) password = "kali";
            
            String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&autoReconnect=true",
                host, port, database
            );
            
            System.out.println("Connecting to database: " + host + ":" + port + "/" + database);
            this.connection = DriverManager.getConnection(url, username, password);
            System.out.println("✓ Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL Driver not found: " + e.getMessage());
            throw new SQLException("MySQL Driver not found", e);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null) {
            instance = new DatabaseConnection();
        } else {
            try {
                if (instance.getConnection().isClosed() || !instance.getConnection().isValid(2)) {
                    System.out.println("Connection lost, reconnecting...");
                    instance = new DatabaseConnection();
                }
            } catch (SQLException e) {
                System.out.println("Connection error, reconnecting...");
                instance = new DatabaseConnection();
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing connection: " + e.getMessage());
        }
    }
}
