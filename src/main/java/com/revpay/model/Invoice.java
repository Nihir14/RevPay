package com.revpay.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Invoice {
    private int invoiceId;
    private int businessId;
    private String customerEmail;
    private BigDecimal amount;
    private String description;
    private String status; // PENDING, PAID
    private Timestamp createdAt;

    public Invoice() {}

    public Invoice(int businessId, String customerEmail, BigDecimal amount, String description) {
        this.businessId = businessId;
        this.customerEmail = customerEmail;
        this.amount = amount;
        this.description = description;
        this.status = "PENDING";
    }

    // Getters and Setters
    public int getInvoiceId() { return invoiceId; }
    public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

    public int getBusinessId() { return businessId; }
    public void setBusinessId(int businessId) { this.businessId = businessId; }

    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return "Invoice #" + invoiceId + " | To: " + customerEmail + " | $" + amount + " | Status: " + status;
    }
}