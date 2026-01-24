package com.revpay.service;

import com.revpay.dao.TransactionDAO;
import com.revpay.model.Transaction;
import java.math.BigDecimal;
import java.util.List;

public class TransactionService {
    private TransactionDAO transactionDAO = new TransactionDAO();
    private UserService userService = new UserService();

    // --- METHOD 1: Transfer by Email (Used in "Send Money" menu) ---
    public boolean processTransfer(int senderId, String receiverEmail, BigDecimal amount) {
        // 1. Get Receiver ID from Email
        int receiverId = userService.getUserIdByEmail(receiverEmail);

        if (receiverId == -1) {
            System.out.println("❌ Receiver email not found.");
            return false;
        }

        // 2. Delegate to the main ID-based method
        return processTransfer(senderId, receiverId, amount);
    }

    // --- METHOD 2: Transfer by ID (Used in "Pay Request" menu & internal calls) ---
    public boolean processTransfer(int senderId, int receiverId, BigDecimal amount) {
        // 1. Validation: Positive Amount
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Amount must be greater than 0.");
            return false;
        }

        // 2. Validation: Prevent sending to self
        if (senderId == receiverId) {
            System.out.println("❌ You cannot send money to yourself.");
            return false;
        }

        // 3. Execute Transaction via DAO
        // (The DAO handles the balance check and rollback logic)
        return transactionDAO.transferMoney(senderId, receiverId, amount);
    }

    // --- Deposit Method ---
    public boolean processDeposit(int userId, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("❌ Deposit amount must be positive.");
            return false;
        }
        return transactionDAO.depositMoney(userId, amount);
    }

    // --- History Method ---
    public List<Transaction> getHistory(int userId) {
        return transactionDAO.getTransactionHistory(userId);
    }
}