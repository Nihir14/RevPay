package com.revpay;

import com.revpay.model.Role;
import com.revpay.model.User;
import com.revpay.service.UserService;
import com.revpay.util.SecurityUtil;

import java.math.BigDecimal; // IMPORT ADDED for money handling
import java.util.Scanner;

public class Main {
    private static UserService userService = new UserService();
    private static Scanner scanner = new Scanner(System.in);
    private static User currentUser = null; // Keeps track of who is logged in

    public static void main(String[] args) {
        while (true) {
            System.out.println("\n=== 🚀 Welcome to RevPay ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    handleRegister();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "3":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

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

            // --- UPDATED: Launch the Dashboard ---
            showUserDashboard();
        }
    }

    private static void handleRegister() {
        System.out.println("\n--- REGISTER ---");
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
        User newUser = new User(email, phone, hashedPassword, pin, name, Role.PERSONAL);

        if (userService.registerUser(newUser)) {
            System.out.println("✅ Registered! Please login.");
        }
    }

    // --- NEW METHOD: The User Menu ---
    private static void showUserDashboard() {
        while (currentUser != null) {
            System.out.println("\n=== 🏦 " + currentUser.getFullName() + "'s Dashboard ===");
            System.out.println("1. Check Balance");
            System.out.println("2. Logout");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    // Call the service to get balance from database
                    BigDecimal balance = userService.getBalance(currentUser.getUserId());
                    System.out.println("\n💰 Current Balance: $" + balance);
                    break;
                case "2":
                    System.out.println("Logging out...");
                    currentUser = null; // This breaks the loop and goes back to Main Menu
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}