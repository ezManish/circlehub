package com.circlehub.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        try (InputStream in = DBConnection.class.getResourceAsStream("/application.properties")) {
            Properties p = new Properties();
            p.load(in);
            DB_URL = p.getProperty("db.url").trim();
            DB_USER = p.getProperty("db.username").trim();
            DB_PASSWORD = p.getProperty("db.password").trim();
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
}
