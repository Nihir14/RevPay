package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.Loan;
import com.revpay.model.LoanStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // 1. Apply for a Loan
    public boolean applyForLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, amount, reason, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, loan.getUserId());
            stmt.setBigDecimal(2, loan.getAmount());
            stmt.setString(3, loan.getReason());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. View all loans for a specific user
    public List<Loan> getLoansByUserId(int userId) {
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Loan loan = new Loan();
                loan.setLoanId(rs.getInt("loan_id"));
                loan.setUserId(rs.getInt("user_id"));
                loan.setAmount(rs.getBigDecimal("amount"));
                loan.setReason(rs.getString("reason"));
                loan.setStatus(LoanStatus.valueOf(rs.getString("status")));
                loan.setAppliedAt(rs.getTimestamp("applied_at"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }
}