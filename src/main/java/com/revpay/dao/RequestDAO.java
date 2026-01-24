package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.PaymentRequest;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RequestDAO {

    // 1. Create a Request
    public boolean createRequest(PaymentRequest req) {
        String sql = "INSERT INTO payment_requests (requester_id, payer_id, amount, status) VALUES (?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, req.getRequesterId());
            stmt.setInt(2, req.getPayerId());
            stmt.setBigDecimal(3, req.getAmount());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 2. Get Requests Sent TO Me (That I need to pay)
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
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
    
    // 3. Update Status (Accept/Decline)
    public boolean updateStatus(int requestId, String status) {
        String sql = "UPDATE payment_requests SET status = ? WHERE request_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, requestId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    // 4. Get Single Request (For verification)
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
                return r;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }
}