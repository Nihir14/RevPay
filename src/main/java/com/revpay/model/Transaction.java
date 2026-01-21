package com.revpay.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private int senderId;
    private int receiverId;
    private BigDecimal amount;
    private TransactionType type;
    private TransactionStatus status;
    private Timestamp timestamp;

    // Constructors
    public Transaction() {}

    public Transaction(int senderId, int receiverId, BigDecimal amount, TransactionType type, TransactionStatus status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }

    @Override
    public String toString() {
        return "Transaction [ID=" + transactionId + ", From=" + senderId + ", To=" + receiverId + ", Amount=" + amount + ", Status=" + status + "]";
    }
}