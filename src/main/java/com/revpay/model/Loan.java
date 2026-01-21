package com.revpay.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Loan {
    private int loanId;
    private int userId;
    private BigDecimal amount;
    private String reason;
    private LoanStatus status;
    private Timestamp appliedAt;

    public Loan() {}

    public Loan(int userId, BigDecimal amount, String reason) {
        this.userId = userId;
        this.amount = amount;
        this.reason = reason;
        this.status = LoanStatus.PENDING; // Default status
    }

    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public Timestamp getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Timestamp appliedAt) { this.appliedAt = appliedAt; }

    @Override
    public String toString() {
        return "Loan [Amount=$" + amount + ", Status=" + status + ", Reason=" + reason + "]";
    }
}