# Bank JDBC Application

## Overview

The **Bank JDBC Application** is a console-based banking system implemented in Java. It allows users to perform various banking operations such as account creation, balance inquiry, fixed deposits, money transfers, and more. The application uses JDBC for database connectivity and includes a passbook system to log transactions locally in text files.

---

## Features

1. **Account Creation:**
   - Users can sign in with personal details like name, age, mobile number, and SSN.
   - Pin and account number are generated during registration.

2. **Login System:**
   - Secure login with account number and PIN.

3. **Banking Operations:**
   - **Balance Inquiry:** Check account balance.
   - **Fixed Deposits:** Make a fixed deposit and calculate maturity value.
   - **Money Transfer:** Transfer money to other accounts securely.
   - **Requests:** Request for bank statements, credit cards, and account updates.

4. **Transaction Logging:**
   - Logs all transactions into a passbook (local text file) for each account.

5. **Database Integration:**
   - Uses JDBC to interact with a database for managing accounts and transaction records.

---

## Prerequisites

- **Java Development Kit (JDK)** 8 or higher
- **JDBC Driver** for your database
- A relational database system like MySQL or PostgreSQL
- Properly configured database with `bank` and `transactions` tables

---

## Setup Instructions

1. **Database Setup:**
   - Create a `bank` table to store account details:
     ```sql
     CREATE TABLE bank (
         Account_no INT PRIMARY KEY,
         Balance DOUBLE
     );
     ```
   - Create a `transactions` table to log transactions:
     ```sql
     CREATE TABLE transactions (
         transaction_id SERIAL PRIMARY KEY,
         account_id INT,
         transaction_type VARCHAR(50),
         Money DOUBLE,
         timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
     );
     ```

2. **Environment Setup:**
   - Install Java and set the `JAVA_HOME` environment variable.
   - Ensure your JDBC driver is added to the project classpath.

3. **Compile and Run:**
   - Compile the `Bankjdbc` class:
     ```bash
     javac Bankjdbc.java
     ```
   - Run the application:
     ```bash
     java Bankjdbc
     ```

---

## How to Use

1. **Account Registration:**
   - Enter your details (name, age, mobile number, SSN).
   - Set a 4-digit PIN and initial balance.

2. **Login:**
   - Provide your account number and PIN.

3. **Menu Options:**
   - Choose from various banking operations:
     - Check balance
     - Make a fixed deposit
     - Transfer money
     - View or update account details
     - Exit the application

---

## Important Notes

- **Age Restriction:** Users must be 18 or older to create an account.
- **Mobile Number Validation:** Mobile numbers must be exactly 10 digits, starting with `1`.
- **Secure PIN:** PIN must be a 4-digit number.
- **Passbook:** A text file with transaction details is generated for each account in the working directory.

---

## File Structure

- **Bankjdbc.java:** Main application file
- **[Account_Number].txt:** Passbook file created for each account

---

## Known Limitations

- Limited error handling for database connectivity.
- Basic input validation (e.g., SSN format).

---

## Future Improvements

- Implement a graphical user interface (GUI) for a better user experience.
- Enhance security measures (e.g., encrypt PINs and sensitive data).
- Expand transaction history capabilities with advanced filtering and exporting.

---

## Author
Ridham Patel
A passionate software engineer with expertise in Java and database management.
