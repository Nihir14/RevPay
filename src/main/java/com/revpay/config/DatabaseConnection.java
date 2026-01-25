package com.revpay.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuration class for handling Database connectivity.
 * <p>
 * This class follows the Singleton pattern concept by providing a global
 * static point of access to the database connection. It utilizes JDBC
 * to establish communication with the MySQL database.
 * </p>
 *
 * @author RevPay Dev Team
 * @version 1.0
 */
public class DatabaseConnection {

    // Initialize Log4j Logger
    private static final Logger logger = LogManager.getLogger(DatabaseConnection.class);

    // Database Configuration Constants
    private static final String URL = "jdbc:mysql://localhost:3306/revpay_db";
    private static final String USER = "root";
    // ⚠️ SECURITY NOTE: In a production environment, fetch this from an environment variable or secret vault.
    private static final String PASSWORD = "Nami1224@@@@";

    /**
     * Establishes and returns a connection to the MySQL database.
     * <p>
     * This method loads the MySQL JDBC driver and attempts to create a connection
     * using the configured credentials. If the connection fails, a FATAL log is recorded.
     * </p>
     *
     * @return A valid {@link Connection} object if successful, or {@code null} if an error occurs.
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Attempt to connect
            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (ClassNotFoundException e) {
            // Fatal error: Missing dependency indicates a broken build
            logger.fatal("❌ MySQL JDBC Driver not found. Ensure the dependency is in pom.xml.", e);
        } catch (SQLException e) {
            // Fatal error: Database connectivity is required for the app to function
            logger.fatal("❌ Database Connection Failed. Verify URL, User, and Password.", e);
        }
        return connection;
    }
}