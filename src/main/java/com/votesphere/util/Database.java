package com.votesphere.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private static final String URL = System.getenv().getOrDefault(
            "VOTESPHERE_DB_URL", "jdbc:mysql://localhost:3306/votesphere?useSSL=false&serverTimezone=UTC");
    private static final String USER = System.getenv().getOrDefault("VOTESPHERE_DB_USER", "root");
    private static final String PASSWORD = System.getenv().getOrDefault("VOTESPHERE_DB_PASSWORD", "");

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
