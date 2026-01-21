package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.Transaction;
import com.revpay.model.TransactionStatus;
import com.revpay.model.TransactionType;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TransactionDAO {

    public boolean transferMoney(int senderId, int receiverId, BigDecimal amount) {
        Connection conn = null;
        PreparedStatement withdrawStmt = null;
        PreparedStatement depositStmt = null;
        PreparedStatement logStmt = null;

        String withdrawSQL = "UPDATE wallets SET balance = balance - ? WHERE user_id = ? AND balance >= ?";
        String depositSQL = "UPDATE wallets SET balance = balance + ? WHERE user_id = ?";
        String logSQL = "INSERT INTO transactions (sender_id, receiver_id, amount, transaction_type, status) VALUES (?, ?, ?, ?, ?)";

        try {
            conn = DatabaseConnection.getConnection();
            
            // 🛑 CRITICAL: Turn off auto-save. We want to save manually only if EVERYTHING works.
            conn.setAutoCommit(false);

            // 1. Withdraw from Sender
            withdrawStmt = conn.prepareStatement(withdrawSQL);
            withdrawStmt.setBigDecimal(1, amount);
            withdrawStmt.setInt(2, senderId);
            withdrawStmt.setBigDecimal(3, amount); // Check if they have enough balance
            int rowsAffected1 = withdrawStmt.executeUpdate();

            if (rowsAffected1 == 0) {
                throw new SQLException("Insufficient funds or invalid sender.");
            }

            // 2. Deposit to Receiver
            depositStmt = conn.prepareStatement(depositSQL);
            depositStmt.setBigDecimal(1, amount);
            depositStmt.setInt(2, receiverId);
            int rowsAffected2 = depositStmt.executeUpdate();

            if (rowsAffected2 == 0) {
                throw new SQLException("Invalid receiver.");
            }

            // 3. Log the Transaction
            logStmt = conn.prepareStatement(logSQL);
            logStmt.setInt(1, senderId);
            logStmt.setInt(2, receiverId);
            logStmt.setBigDecimal(3, amount);
            logStmt.setString(4, TransactionType.TRANSFER.name());
            logStmt.setString(5, TransactionStatus.SUCCESS.name());
            logStmt.executeUpdate();

            // ✅ If we get here, everything worked! Commit the changes.
            conn.commit();
            return true;

        } catch (SQLException e) {
            // ❌ If ANY error happened, UNDO everything.
            if (conn != null) {
                try {
                    System.out.println("⚠️ Transaction failed. Rolling back money...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            // Close everything to prevent memory leaks
            try {
                if (withdrawStmt != null) withdrawStmt.close();
                if (depositStmt != null) depositStmt.close();
                if (logStmt != null) logStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset to default
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}