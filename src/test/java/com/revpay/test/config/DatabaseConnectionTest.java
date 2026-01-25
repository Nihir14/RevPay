package com.revpay.test.config; // Ensure this matches your folder structure

import org.junit.Test;
import java.sql.Connection;
import java.sql.SQLException;

// 1. Explicit Import (Fixes the resolution error)
import com.revpay.config.DatabaseConnection;

import static org.junit.Assert.*;

/**
 * Unit/Integration tests for {@link com.revpay.config.DatabaseConnection}.
 * <p>
 * This test class verifies that the application can successfully establish
 * a connection to the configured MySQL database.
 * </p>
 */
public class DatabaseConnectionTest {

    /**
     * Test Scenario: Verify that a valid connection is returned.
     * <p>
     * <b>Pre-requisite:</b> The MySQL server must be running and the
     * credentials in {@link com.revpay.config.DatabaseConnection} must be correct.
     * </p>
     */
    @Test
    public void testGetConnection() {
        // 1. Attempt to get connection
        Connection conn = DatabaseConnection.getConnection();

        // 2. Assertions
        assertNotNull("Connection object should not be null. Check DB credentials.", conn);

        try {
            assertFalse("Connection should be open (not closed)", conn.isClosed());
            System.out.println("✅ Database Connection Test Passed!");

            // Clean up
            conn.close();
        } catch (SQLException e) {
            fail("SQLException occurred during test: " + e.getMessage());
        }
    }
}