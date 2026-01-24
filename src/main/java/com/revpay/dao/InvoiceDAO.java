package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.Invoice;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {

    // 1. Create Invoice
    public boolean createInvoice(Invoice inv) {
        String sql = "INSERT INTO invoices (business_id, customer_email, amount, description, status) VALUES (?, ?, ?, ?, 'PENDING')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, inv.getBusinessId());
            stmt.setString(2, inv.getCustomerEmail());
            stmt.setBigDecimal(3, inv.getAmount());
            stmt.setString(4, inv.getDescription());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // 2. Get Invoices for the Business (To see what they sent)
    public List<Invoice> getInvoicesByBusiness(int businessId) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE business_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, businessId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 3. Get Invoices for the Customer (To see what they owe)
    public List<Invoice> getInvoicesForCustomer(String email) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE customer_email = ? AND status = 'PENDING'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // 4. Mark as Paid
    public boolean markAsPaid(int invoiceId) {
        String sql = "UPDATE invoices SET status = 'PAID' WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, invoiceId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
    
    // 5. Get Single Invoice
    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {}
        return null;
    }

    // Helper to map DB row to Java Object
    private Invoice mapRow(ResultSet rs) throws SQLException {
        Invoice i = new Invoice();
        i.setInvoiceId(rs.getInt("invoice_id"));
        i.setBusinessId(rs.getInt("business_id"));
        i.setCustomerEmail(rs.getString("customer_email"));
        i.setAmount(rs.getBigDecimal("amount"));
        i.setDescription(rs.getString("description"));
        i.setStatus(rs.getString("status"));
        i.setCreatedAt(rs.getTimestamp("created_at"));
        return i;
    }
}