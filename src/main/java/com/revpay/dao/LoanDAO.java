package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.Loan;
import com.revpay.model.LoanStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Loan operations.
 * <p>
 * This class handles database interactions related to loan applications
 * and retrieval. It allows Business users to apply for loans and view
 * their application history.
 * </p>
 *
 * @author RevPay Dev Team
 * @version 1.0
 */
public class LoanDAO {

    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(LoanDAO.class);

    /**
     * Submits a new loan application for a user.
     * <p>
     * The loan is inserted with a default status of 'PENDING'.
     * </p>
     *
     * @param loan The {@link Loan} object containing the user ID, amount, and reason.
     * @return {@code true} if the application was successfully saved, {@code false} otherwise.
     */
    public boolean applyForLoan(Loan loan) {
        String sql = "INSERT INTO loans (user_id, amount, reason, status) VALUES (?, ?, ?, 'PENDING')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loan.getUserId());
            stmt.setBigDecimal(2, loan.getAmount());
            stmt.setString(3, loan.getReason());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("✅ Loan Application Submitted: User ID " + loan.getUserId() + " requested $" + loan.getAmount());
                return true;
            }
        } catch (SQLException e) {
            logger.error("❌ Failed to submit loan application for User ID " + loan.getUserId(), e);
        }
        return false;
    }

    /**
     * Retrieves all loan applications associated with a specific user.
     *
     * @param userId The unique ID of the user.
     * @return A {@link List} of {@link Loan} objects. Returns an empty list if no loans are found.
     */
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

                // Safely parse enum string
                try {
                    loan.setStatus(LoanStatus.valueOf(rs.getString("status")));
                } catch (IllegalArgumentException e) {
                    logger.warn("Unknown loan status found in DB: " + rs.getString("status"));
                }

                loan.setAppliedAt(rs.getTimestamp("applied_at"));
                loans.add(loan);
            }
        } catch (SQLException e) {
            logger.error("❌ Error retrieving loans for User ID " + userId, e);
        }
        return loans;
    }
}