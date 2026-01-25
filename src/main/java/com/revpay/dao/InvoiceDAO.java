package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.Invoice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Invoices in the RevPay system.
 * <p>
 * This class handles all CRUD (Create, Read, Update, Delete) operations related
 * to the {@link Invoice} model. It allows businesses to issue invoices,
 * customers to view pending bills, and the system to update payment statuses.
 * </p>
 *
 * @author RevPay Dev Team
 * @version 1.0
 */
public class InvoiceDAO {

    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(InvoiceDAO.class);

    /**
     * Creates a new invoice in the database.
     * <p>
     * The invoice status is set to 'PENDING' by default.
     * </p>
     *
     * @param inv The {@link Invoice} object containing details (Business ID, Customer Email, Amount, etc.).
     * @return {@code true} if the invoice was successfully created, {@code false} otherwise.
     */
    public boolean createInvoice(Invoice inv) {
        String sql = "INSERT INTO invoices (business_id, customer_email, amount, description, status) VALUES (?, ?, ?, ?, 'PENDING')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, inv.getBusinessId());
            stmt.setString(2, inv.getCustomerEmail());
            stmt.setBigDecimal(3, inv.getAmount());
            stmt.setString(4, inv.getDescription());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                logger.info("✅ Invoice Created: $" + inv.getAmount() + " for " + inv.getCustomerEmail());
                return true;
            }
        } catch (SQLException e) {
            logger.error("❌ Failed to create invoice for customer: " + inv.getCustomerEmail(), e);
        }
        return false;
    }

    /**
     * Retrieves all invoices issued by a specific business.
     *
     * @param businessId The unique ID of the business user.
     * @return A {@link List} of invoices created by the business. Returns an empty list if none found.
     */
    public List<Invoice> getInvoicesByBusiness(int businessId) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE business_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, businessId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("❌ Error fetching invoices for Business ID: " + businessId, e);
        }
        return list;
    }

    /**
     * Retrieves all PENDING invoices for a specific customer.
     * <p>
     * This is used to show the customer what bills they need to pay.
     * </p>
     *
     * @param email The email address of the customer.
     * @return A {@link List} of pending invoices.
     */
    public List<Invoice> getInvoicesForCustomer(String email) {
        List<Invoice> list = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE customer_email = ? AND status = 'PENDING'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            logger.error("❌ Error fetching pending invoices for customer: " + email, e);
        }
        return list;
    }

    /**
     * Updates the status of an invoice to 'PAID'.
     *
     * @param invoiceId The unique ID of the invoice to update.
     * @return {@code true} if the update was successful, {@code false} otherwise.
     */
    public boolean markAsPaid(int invoiceId) {
        String sql = "UPDATE invoices SET status = 'PAID' WHERE invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, invoiceId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                logger.info("✅ Invoice #" + invoiceId + " marked as PAID.");
                return true;
            }
        } catch (SQLException e) {
            logger.error("❌ Failed to mark invoice #" + invoiceId + " as paid.", e);
        }
        return false;
    }

    /**
     * Retrieves a single invoice by its ID.
     *
     * @param id The unique ID of the invoice.
     * @return The {@link Invoice} object if found, or {@code null} if not found.
     */
    public Invoice getInvoiceById(int id) {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            logger.error("❌ Error fetching Invoice ID: " + id, e);
        }
        return null;
    }

    /**
     * Helper method to map a ResultSet row to an Invoice object.
     *
     * @param rs The ResultSet positioned at the current row.
     * @return An mapped {@link Invoice} object.
     * @throws SQLException If a database access error occurs.
     */
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