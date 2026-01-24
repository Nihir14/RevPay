package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {

    // Method to register a new user
    public boolean registerUser(User user) {
        String sql = "INSERT INTO users (email, phone_number, password_hash, transaction_pin, full_name, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // We replace the '?' with actual data
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPhoneNumber());
            stmt.setString(3, user.getPasswordHash()); // This must be already hashed
            stmt.setString(4, user.getTransactionPin());
            stmt.setString(5, user.getFullName());
            stmt.setString(6, user.getRole().name()); // Converts Enum to String ("PERSONAL" or "BUSINESS")

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Returns true if the user was successfully added

        } catch (SQLException e) {
            System.out.println("❌ Error registering user: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NEW METHOD: Find a user by their email
    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            var rs = stmt.executeQuery();

            if (rs.next()) {
                // If found, map the database row to our Java User object
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phone_number"));
                user.setPasswordHash(rs.getString("password_hash")); // We need this to verify login
                user.setTransactionPin(rs.getString("transaction_pin"));
                user.setFullName(rs.getString("full_name"));
                user.setRole(com.revpay.model.Role.valueOf(rs.getString("role")));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if user not found
    }

    // Add this inside UserDAO.java
    // ... inside UserDAO class ...

    // ... inside UserDAO ...

    public boolean deleteUser(int userId) {
        String[] sqlSteps = {
                "DELETE FROM payment_methods WHERE user_id = ?",
                "DELETE FROM payment_requests WHERE requester_id = ? OR payer_id = ?",
                "DELETE FROM invoices WHERE business_id = ?",
                "DELETE FROM transactions WHERE sender_id = ? OR receiver_id = ?",
                "DELETE FROM loans WHERE user_id = ?",
                "DELETE FROM business_profiles WHERE user_id = ?",
                "DELETE FROM wallets WHERE user_id = ?",
                "DELETE FROM users WHERE user_id = ?"
        };

        // 1. Declare OUTSIDE try block so catch can see it
        java.sql.Connection conn = null;

        try {
            conn = com.revpay.config.DatabaseConnection.getConnection();

            // 2. Start Transaction
            conn.setAutoCommit(false);

            for (String sql : sqlSteps) {
                try (java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, userId);
                    // Special cases for OR conditions
                    if (sql.contains("OR payer_id") || sql.contains("OR receiver_id")) {
                        stmt.setInt(2, userId);
                    }
                    stmt.executeUpdate();
                }
            }

            // 3. Commit Success
            conn.commit();
            return true;

        } catch (java.sql.SQLException e) {
            e.printStackTrace();
            // 4. Now 'conn' is visible here for Rollback
            if (conn != null) {
                try {
                    System.out.println("⚠️ Error deleting. Rolling back changes...");
                    conn.rollback();
                } catch (java.sql.SQLException ex) { ex.printStackTrace(); }
            }
            return false;

        } finally {
            // 5. Always close connection manually since we didn't use try-with-resources for it
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // Good practice to reset
                    conn.close();
                } catch (java.sql.SQLException e) { e.printStackTrace(); }
            }
        }
    }
}