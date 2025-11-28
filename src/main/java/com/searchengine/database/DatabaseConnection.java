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
            
            // Railway provides MYSQL_* variables, fallback to custom or local
            String host = System.getenv("MYSQL_HOST");
            String port = System.getenv("MYSQL_PORT");
            String database = System.getenv("MYSQL_DATABASE");
            String username = System.getenv("MYSQL_USER");
            String password = System.getenv("MYSQL_PASSWORD");
            
            // Fallback to custom variables or local
            if (host == null) host = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
            if (port == null) port = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "3306";
            if (database == null) database = System.getenv("DB_NAME") != null ? System.getenv("DB_NAME") : "searchengine_db";
            if (username == null) username = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "root";
            if (password == null) password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "kali";
            
            String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
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
        } else if (instance.getConnection().isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}
