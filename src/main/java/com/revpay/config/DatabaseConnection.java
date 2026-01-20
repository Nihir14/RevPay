package com.revpay.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    // 1. Database Credentials
    private static final String URL = "jdbc:mysql://localhost:3306/revpay_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Nami1224@@@@"; // <--- CHANGE THIS

    // 2. The Connection Method
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL Driver (Optional in newer Java versions, but good practice)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Attempt to connect
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL Driver not found. Check your pom.xml dependencies.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Connection Failed! Check your database name, user, or password.");
            e.printStackTrace();
        }
        return connection;
    }
}