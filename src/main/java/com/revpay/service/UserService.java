package com.revpay.service;

import com.revpay.dao.UserDAO;
import com.revpay.dao.WalletDAO;
import com.revpay.model.User;
import com.revpay.util.SecurityUtil;

import java.math.BigDecimal;

public class UserService {
    private UserDAO userDAO = new UserDAO();

    public User login(String email, String password) {
        // 1. Get the user from the DB
        User user = userDAO.getUserByEmail(email);

        // 2. Check if user exists
        if (user == null) {
            System.out.println("❌ Account not found.");
            return null;
        }

        // 3. Verify the password
        if (SecurityUtil.verifyPassword(password, user.getPasswordHash())) {
            return user; // Login successful! Return the user object.
        } else {
            System.out.println("❌ Wrong password.");
            return null;
        }
    }
    
    // Wrapper for registration to keep Main clean
//    public boolean registerUser(User user) {
//        return userDAO.registerUser(user);
//    }

    // ... inside UserService class ...
    private WalletDAO walletDAO = new WalletDAO(); // Add this line at the top

    public boolean registerUser(User user) {
        // 1. Register the User
        boolean isRegistered = userDAO.registerUser(user);

        // 2. If successful, find their ID and create a Wallet
        if (isRegistered) {
            User savedUser = userDAO.getUserByEmail(user.getEmail()); // Fetch to get the new ID
            if (savedUser != null) {
                walletDAO.createWallet(savedUser.getUserId());
            }
        }
        return isRegistered;
    }

    // Add a wrapper to get balance easily
    public BigDecimal getBalance(int userId) {
        return walletDAO.getBalance(userId);
    }

    // ... inside UserService ...

    // Helper: Find User ID by Email
    public int getUserIdByEmail(String email) {
        User user = userDAO.getUserByEmail(email);
        if (user != null) {
            return user.getUserId();
        }
        return -1; // Return -1 if not found
    }
}