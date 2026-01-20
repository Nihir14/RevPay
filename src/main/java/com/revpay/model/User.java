package com.revpay.model;

public class User {
    private int userId;
    private String email;
    private String phoneNumber;
    private String passwordHash; // We store the encrypted password, not the real one
    private String transactionPin;
    private String fullName;
    private Role role; // PERSONAL or BUSINESS

    // 1. Empty Constructor (Needed for database tools later)
    public User() {}

    // 2. Full Constructor (Used when we create a new user in the app)
    public User(String email, String phoneNumber, String passwordHash, String transactionPin, String fullName, Role role) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.transactionPin = transactionPin;
        this.fullName = fullName;
        this.role = role;
    }

    // 3. Getters and Setters (So other parts of the app can read/write this data)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getTransactionPin() { return transactionPin; }
    public void setTransactionPin(String transactionPin) { this.transactionPin = transactionPin; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    
    // 4. toString (Helps us print the user details easily for debugging)
    @Override
    public String toString() {
        return "User{" +
                "id=" + userId +
                ", email='" + email + '\'' +
                ", name='" + fullName + '\'' +
                ", role=" + role +
                '}';
    }
}