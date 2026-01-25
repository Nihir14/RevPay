# 🚀 RevPay - Digital Banking System

**RevPay** is a console-based banking application built with Java. It simulates a core financial platform allowing users to send money, pay invoices, request funds, and manage their digital wallets securely.

---

## 🛠 Tech Stack
* **Language:** Java 21
* **Database:** MySQL 8.0
* **Build Tool:** Maven
* **Testing:** JUnit 4
* **Logging:** Log4j2
* **Security:** BCrypt (Password Hashing)

---

## ✨ Key Features

### 👤 User Features
* **Secure Authentication:** Registration & Login with encrypted passwords.
* **Digital Wallet:** Auto-created upon registration. Check balance & Deposit funds.
* **P2P Transfers:** Send money to other users via email.
* **Request Money:** Ask other users for funds (Approve/Decline requests).
* **Transaction History:** View detailed logs of all transfers.
* **Card Management:** Save Credit/Debit cards (stored securely).

### 🏢 Business Features
* **Invoicing:** Business accounts can issue invoices to customers.
* **Loans:** Apply for business loans and track status.

---

## ⚙️ Setup Instructions

### 1. Database Setup
1.  Open MySQL Workbench.
2.  Create a new database named `revpay`.
3.  Run the provided `setup.sql` script to create the necessary tables.
4.  Update `src/main/java/com/revpay/config/DatabaseConnection.java` with your MySQL username and password.

### 2. Build the Project
Open your terminal in the project folder and run:
```bash
mvn clean install