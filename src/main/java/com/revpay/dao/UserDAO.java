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
}