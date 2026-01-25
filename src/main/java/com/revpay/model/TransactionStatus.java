package com.revpay.model;

/**
 * Enumeration representing the final outcome of a financial transaction.
 * <p>
 * This is used to track whether money was successfully moved, if the operation failed,
 * or if it is still processing.
 * </p>
 *
 * @author RevPay Dev Team
 * @version 1.0
 */
public enum TransactionStatus {

    /**
     * The transaction completed successfully. Money has been moved.
     */
    SUCCESS,

    /**
     * The transaction failed (e.g., insufficient funds, database error).
     * No money was moved.
     */
    FAILED,

    /**
     * The transaction is currently in progress or waiting for approval.
     */
    PENDING
}