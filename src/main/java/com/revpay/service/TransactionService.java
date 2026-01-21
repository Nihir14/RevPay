package com.revpay.service;

import com.revpay.dao.TransactionDAO;
import java.math.BigDecimal;

public class TransactionService {
    private TransactionDAO transactionDAO = new TransactionDAO();
    private UserService userService = new UserService();

    public boolean processTransfer(int senderId, String receiverEmail, BigDecimal amount) {
        // 1. Validation: Positive Amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Amount must be greater than 0.");
            return false;
        }

        // 2. Validation: Find Receiver
        int receiverId = userService.getUserIdByEmail(receiverEmail);
        if (receiverId == -1) {
            System.out.println("❌ Receiver email not found.");
            return false;
        }

        // 3. Validation: prevent sending to self
        if (senderId == receiverId) {
            System.out.println("❌ You cannot send money to yourself.");
            return false;
        }

        // 4. Execute Transaction
        return transactionDAO.transferMoney(senderId, receiverId, amount);
    }

    // ... inside TransactionService ...

    public java.util.List<com.revpay.model.Transaction> getHistory(int userId) {
        return transactionDAO.getTransactionHistory(userId);
    }

    // ... inside TransactionService ...

    public boolean processDeposit(int userId, BigDecimal amount) {
        // 1. Validation: Positive Amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Deposit amount must be positive.");
            return false;
        }

        // 2. Call DAO
        return transactionDAO.depositMoney(userId, amount);
    }
}