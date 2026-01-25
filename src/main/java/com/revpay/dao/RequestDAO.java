package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.PaymentRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Payment Requests.
 * <p>
 * This class facilitates the "Request Money" feature, allowing users to ask for funds
 * from other users. It handles creating requests, retrieving pending requests for a user,
 * and updating the status (e.g., ACCEPTED, DECLINED).
 * </p>
 *
 * @author RevPay Dev Team
 * @version 1.0
 */
public class RequestDAO {

    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(RequestDAO.class);

    /**
     * Creates a new payment request in the database.
     *
     * @param req The {@link PaymentRequest} object containing requester and payer IDs and amount.
     * @return {@code true} if the request was successfully created, {@code false} otherwise.
     */
    public boolean createRequest(PaymentRequest req) {
        String sql = "INSERT INTO payment_requests (requester_id, payer_id, amount, status) VALUES (?, ?, ?, 'PENDING')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, req.getRequesterId());
            stmt.setInt(2, req.getPayerId());
            stmt.setBigDecimal(3, req.getAmount());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("✅ Payment Request Created: User " + req.getRequesterId() + " asked User " + req.getPayerId() + " for $" + req.getAmount());
                return true;
            }
        } catch (SQLException e) {
            logger.error("❌ Failed to create payment request from User " + req.getRequesterId() + " to " + req.getPayerId(), e);
        }
        return false;
    }

    /**
     * Retrieves all PENDING payment requests sent TO a specific user.
     * <p>
     * These are the requests that the user needs to pay or decline.
     * </p>
     *
     * @param userId The ID of the user who received the requests (the payer).
     * @return A {@link List} of pending {@link PaymentRequest} objects.
     */
    public List<PaymentRequest> getIncomingRequests(int userId) {
        List<PaymentRequest> list = new ArrayList<>();
        String sql = "SELECT * FROM payment_requests WHERE payer_id = ? AND status = 'PENDING'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PaymentRequest r = new PaymentRequest();
                r.setRequestId(rs.getInt("request_id"));
                r.setRequesterId(rs.getInt("requester_id"));
                r.setPayerId(rs.getInt("payer_id"));
                r.setAmount(rs.getBigDecimal("amount"));
                r.setStatus(rs.getString("status"));
                list.add(r);
            }
        } catch (SQLException e) {
            logger.error("❌ Error fetching incoming requests for User ID " + userId, e);
        }
        return list;
    }

    /**
     * Updates the status of a payment request (e.g., to 'ACCEPTED' or 'DECLINED').
     *
     * @param requestId The unique ID of the request.
     * @param status The new status string.
     * @return {@code true} if the update was successful, {@code false} otherwise.
     */
    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE payment_requests SET status = ? WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, requestId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("✅ Request ID " + requestId + " updated to status: " + status);
                return true;
            }
        } catch (SQLException e) {
            logger.error("❌ Failed to update status for Request ID " + requestId, e);
        }
        return false;
    }

    /**
     * Retrieves a single payment request by its ID.
     *
     * @param requestId The unique ID of the request.
     * @return The {@link PaymentRequest} object if found, or {@code null} if not found.
     */
    public PaymentRequest getRequestById(int requestId) {
        String sql = "SELECT * FROM payment_requests WHERE request_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                PaymentRequest r = new PaymentRequest();
                r.setRequestId(rs.getInt("request_id"));
                r.setRequesterId(rs.getInt("requester_id"));
                r.setPayerId(rs.getInt("payer_id"));
                r.setAmount(rs.getBigDecimal("amount"));
                r.setStatus(rs.getString("status")); // Ensure status is also retrieved
                return r;
            }
        } catch (SQLException e) {
            logger.error("❌ Error fetching Request ID " + requestId, e);
        }
        return null;
    }
}