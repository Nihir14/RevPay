package com.revpay;

import com.revpay.dao.LoanDAO;
import com.revpay.model.Role;
import com.revpay.model.User;
import com.revpay.model.Loan;
import com.revpay.model.Transaction;
import com.revpay.service.TransactionService;
import com.revpay.service.UserService;
import com.revpay.util.SecurityUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Main {
    // --- SERVICES & DAOS ---
    private static UserService userService = new UserService();
    private static TransactionService transactionService = new TransactionService();
    private static LoanDAO loanDAO = new LoanDAO();

    // --- TOOLS ---
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== 🚀 Welcome to RevPay ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1": handleRegister(); break;
                case "2": handleLogin(); break;
                case "3": System.out.println("Goodbye!"); return;
                default: System.out.println("Invalid option.");
            }
        }
    }

    // ==========================================
    //       AUTHENTICATION
    // ==========================================

    private static void handleLogin() {
        System.out.println("\n--- LOGIN ---");
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        User user = userService.login(email, password);
        if (user != null) {
            currentUser = user;
            System.out.println("✅ Login Successful! Welcome, " + user.getFullName());
            showUserDashboard();
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- REGISTER ---");
        System.out.println("Select Account Type:\n1. Personal\n2. Business");
        System.out.print("Choice: ");
        String roleChoice = scanner.nextLine();
        Role role = roleChoice.equals("2") ? Role.BUSINESS : Role.PERSONAL;

        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        System.out.print("Enter Full Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Password: ");
        String rawPassword = scanner.nextLine();
        System.out.print("Enter Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Enter 4-digit PIN: ");
        String pin = scanner.nextLine();

        String hashedPassword = SecurityUtil.hashPassword(rawPassword);
        User newUser = new User(email, phone, hashedPassword, pin, name, role);

        if (userService.registerUser(newUser)) {
            System.out.println("✅ Registered Successfully! Please login.");
        }
    }

    // ==========================================
    //           USER DASHBOARD
    // ==========================================

    private static void showUserDashboard() {
        while (currentUser != null) {
            System.out.println("\n=== 🏦 " + currentUser.getFullName() + "'s Dashboard ===");

            System.out.println("1. Check Balance");
            System.out.println("2. Add Money"); // <--- NEW OPTION
            System.out.println("3. Send Money");
            System.out.println("4. View Transaction History");

            if (currentUser.getRole() == Role.BUSINESS) {
                System.out.println("--- Business Services ---");
                System.out.println("5. Apply for Loan");
                System.out.println("6. View My Loans");
                System.out.println("-------------------------");
                System.out.println("7. Logout");
            } else {
                System.out.println("5. Logout");
            }

            System.out.print("Choose an option: ");
            String choice = scanner.nextLine();

            // Unified switch since options 1-4 are the same for everyone
            switch (choice) {
                case "1": checkBalance(); break;
                case "2": handleAddMoney(); break; // <--- Calls new method
                case "3": handleSendMoney(); break;
                case "4": handleViewHistory(); break;
                case "5":
                    if (currentUser.getRole() == Role.BUSINESS) handleApplyLoan();
                    else logout();
                    break;
                case "6":
                    if (currentUser.getRole() == Role.BUSINESS) handleViewLoans();
                    else System.out.println("Invalid option.");
                    break;
                case "7":
                    if (currentUser.getRole() == Role.BUSINESS) logout();
                    else System.out.println("Invalid option.");
                    break;
                default: System.out.println("❌ Invalid option.");
            }
        }
    }

    // ==========================================
    //           FEATURES
    // ==========================================

    private static void checkBalance() {
        BigDecimal balance = userService.getBalance(currentUser.getUserId());
        System.out.println("\n💰 Current Balance: $" + balance);
    }

    private static void logout() {
        System.out.println("Logging out...");
        currentUser = null;
    }

    // --- NEW: Add Money Feature ---
    private static void handleAddMoney() {
        System.out.println("\n--- ➕ ADD MONEY ---");
        System.out.print("Enter Amount to Deposit: ");
        try {
            String amountStr = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountStr);

            // Simulate Card Entry (Mock)
            System.out.print("Enter Card Number (Simulated): ");
            scanner.nextLine(); // Just consume the input
            System.out.println("Processing with Bank...");

            if (transactionService.processDeposit(currentUser.getUserId(), amount)) {
                System.out.println("✅ Deposit Successful!");
                checkBalance(); // Show new balance
            } else {
                System.out.println("❌ Deposit Failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount format.");
        }
    }

    private static void handleSendMoney() {
        System.out.println("\n--- 💸 SEND MONEY ---");
        System.out.print("Enter Receiver's Email: ");
        String receiverEmail = scanner.nextLine();
        System.out.print("Enter Amount: ");
        try {
            String amountStr = scanner.nextLine();
            BigDecimal amount = new BigDecimal(amountStr);

            if (transactionService.processTransfer(currentUser.getUserId(), receiverEmail, amount)) {
                System.out.println("✅ Transfer Successful!");
                checkBalance();
            } else {
                System.out.println("❌ Transfer Failed.");
            }
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount.");
        }
    }

    private static void handleViewHistory() {
        System.out.println("\n--- 📜 TRANSACTION HISTORY ---");
        List<Transaction> history = transactionService.getHistory(currentUser.getUserId());

        if (history.isEmpty()) {
            System.out.println("No transactions found.");
        } else {
            System.out.printf("%-5s | %-10s | %-10s | %-10s | %-15s%n", "ID", "Type", "Amount", "Status", "Date");
            System.out.println("-------------------------------------------------------------------");
            for (Transaction t : history) {
                String sign = (t.getType().toString().equals("DEPOSIT") || t.getReceiverId() == currentUser.getUserId() && t.getType().toString().equals("TRANSFER")) ? "+" : "-";

                System.out.printf("%-5d | %-10s | %s$%-9s | %-10s | %s%n",
                        t.getTransactionId(), t.getType(), sign, t.getAmount(), t.getStatus(), t.getTimestamp().toString().substring(0, 16));
            }
        }
    }

    private static void handleApplyLoan() {
        System.out.println("\n--- 💼 APPLY FOR LOAN ---");
        System.out.print("Enter Loan Amount: ");
        try {
            BigDecimal amount = new BigDecimal(scanner.nextLine());
            System.out.print("Reason: ");
            String reason = scanner.nextLine();
            if (loanDAO.applyForLoan(new Loan(currentUser.getUserId(), amount, reason))) {
                System.out.println("✅ Application Submitted!");
            } else {
                System.out.println("❌ Failed.");
            }
        } catch (Exception e) { System.out.println("❌ Invalid input."); }
    }

    private static void handleViewLoans() {
        System.out.println("\n--- 📜 LOAN HISTORY ---");
        List<Loan> loans = loanDAO.getLoansByUserId(currentUser.getUserId());
        if (loans.isEmpty()) System.out.println("No loans found.");
        else for (Loan l : loans) System.out.println("Loan ID: " + l.getLoanId() + " | Amount: $" + l.getAmount() + " | Status: " + l.getStatus());
    }
}