package com.revpay.model;

import java.sql.Date;

public class PaymentMethod {
    private int methodId;
    private int userId;
    private String cardNumber; // We will store this encrypted
    private String cardType;   // CREDIT or DEBIT
    private Date expiryDate;
    
    public PaymentMethod() {}

    public PaymentMethod(int userId, String cardNumber, String cardType, Date expiryDate) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.expiryDate = expiryDate;
    }

    // Getters and Setters
    public int getMethodId() { return methodId; }
    public void setMethodId(int methodId) { this.methodId = methodId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getCardNumber() { return cardNumber; }
    public void setCardNumber(String cardNumber) { this.cardNumber = cardNumber; }
    
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
    
    public Date getExpiryDate() { return expiryDate; }
    public void setExpiryDate(Date expiryDate) { this.expiryDate = expiryDate; }

    @Override
    public String toString() {
        // Mask the card number for display (e.g., ************1234)
        String masked = (cardNumber != null && cardNumber.length() > 4) 
                ? "****-****-****-" + cardNumber.substring(cardNumber.length() - 4) 
                : "****";
        return "[" + cardType + "] " + masked + " (Exp: " + expiryDate + ")";
    }
}