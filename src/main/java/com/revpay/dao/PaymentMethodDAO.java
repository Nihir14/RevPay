package com.revpay.dao;

import com.revpay.config.DatabaseConnection;
import com.revpay.model.PaymentMethod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for managing Payment Methods (Credit/Debit Cards).
 * <p>
 * This class handles the secure storage and retrieval of user payment information.
 * <br>
 * <b>Security Note:</b> In a production environment, the {@code card_number_encrypted}
 * column should store securely hashed or encrypted data, not plain text.
 * </p>
 *
 * @author RevPay Dev Team
 * @version 1.0
 */
public class PaymentMethodDAO {

    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(PaymentMethodDAO.class);

    /**
     * Adds a new payment method (Card) to the user's account.
     *
     * @param pm The {@link PaymentMethod} object containing card details.
     * @return {@code true} if the card was successfully saved, {@code false} otherwise.
     */
    public boolean addPaymentMethod(PaymentMethod pm) {
        String sql = "INSERT INTO payment_methods (user_id, card_number_encrypted, card_type, expiry_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, pm.getUserId());
            // TODO: Integrate AES Encryption Service here for Production
            stmt.setString(2, pm.getCardNumber());
            stmt.setString(3, pm.getCardType());
            stmt.setDate(4, pm.getExpiryDate());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                // Log success but MASK the card number for security logs
                String maskedCard = "****" + pm.getCardNumber().substring(pm.getCardNumber().length() - 4);
                logger.info("✅ Payment Method Added: " + pm.getCardType() + " ending in " + maskedCard + " for User ID " + pm.getUserId());
                return true;
            }
        } catch (SQLException e) {
            logger.error("❌ Failed to add payment method for User ID " + pm.getUserId(), e);
        }
        return false;
    }

    /**
     * Retrieves all payment methods associated with a specific user.
     *
     * @param userId The unique ID of the user.
     * @return A {@link List} of {@link PaymentMethod} objects. Returns an empty list if none found.
     */
    public List<PaymentMethod> getMethodsByUserId(int userId) {
        List<PaymentMethod> list = new ArrayList<>();
        String sql = "SELECT * FROM payment_methods WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PaymentMethod pm = new PaymentMethod();
                pm.setMethodId(rs.getInt("method_id"));
                pm.setUserId(rs.getInt("user_id"));
                pm.setCardNumber(rs.getString("card_number_encrypted"));
                pm.setCardType(rs.getString("card_type"));
                pm.setExpiryDate(rs.getDate("expiry_date"));
                list.add(pm);
            }
        } catch (SQLException e) {
            logger.error("❌ Error fetching payment methods for User ID " + userId, e);
        }
        return list;
    }
}