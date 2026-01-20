package com.revpay.model;

import java.math.BigDecimal;

public class Wallet {
    private int walletId;
    private int userId;
    private BigDecimal balance;

    public Wallet() {}

    public Wallet(int userId, BigDecimal balance) {
        this.userId = userId;
        this.balance = balance;
    }

    public int getWalletId() { return walletId; }
    public void setWalletId(int walletId) { this.walletId = walletId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
}