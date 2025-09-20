package com.gympandro.app.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Db {
    private static String url, user, pass;

    public static void init(String u, String usr, String pw) {
        url = u;
        user = usr;
        pass = pw;
        try {
            DriverManager.getConnection(url, user, pass).close();
            System.out.println("DB connected: " + url);
        } catch (SQLException e) {
            throw new RuntimeException("DB connection failed: " + e.getMessage(), e);
        }
    }

    public static Connection conn() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}