package com.revpay.model;

import java.math.BigDecimal;

public class PaymentRequest {
    private int requestId;
    private int requesterId;
    private int payerId;
    private BigDecimal amount;
    private String status; // PENDING, ACCEPTED, DECLINED

    public PaymentRequest() {}

    public PaymentRequest(int requesterId, int payerId, BigDecimal amount) {
        this.requesterId = requesterId;
        this.payerId = payerId;
        this.amount = amount;
        this.status = "PENDING";
    }

    // Getters/Setters
    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public int getRequesterId() { return requesterId; }
    public void setRequesterId(int requesterId) { this.requesterId = requesterId; }
    public int getPayerId() { return payerId; }
    public void setPayerId(int payerId) { this.payerId = payerId; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return "ID: " + requestId + " | Amount: $" + amount + " | Status: " + status;
    }
}