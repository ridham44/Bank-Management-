//A Project Report On Online Bank System Code
// By Ridham Patel B-9(259) CST. 
//  09/10/23 

import java.sql.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.Date;

class Bankjdbc {
	// Class variables
	static String Name, MobileNo, Ssn;
	static double Si, Result, Balance, Money;
	static int Account_no = 220010103, count = 0;
	static int Age, Epin, Acc_no, Sacc_no, Year, Pin, Tpin, Bacc_no;

	// Database connection objects
	static Statement st;
	static Connection con;
	static PreparedStatement pst;

	// File writer for transactions log
	BufferedWriter writer = new BufferedWriter(new FileWriter(Account_no + ".txt", true));
	static Hashtable<Integer, Double> accounts;

	// Scanner for user input
	static Scanner sc = new Scanner(System.in);

	// Constructor
	Bankjdbc() throws Exception {
		accounts = new Hashtable<>();
		accounts.put(Account_no, Balance);
	}

	// Enter method
	void enter() throws Exception {
		System.out.println("Let's Start");
		this.signin();
	}

	// Signin method
	void signin() throws Exception {
		System.out.println("-----------Signin----------");
		System.out.println("Enter your Name");

		// Consume newline character if count is greater than or equal to 1
		if (count >= 1)
			sc.nextLine();

		Name = sc.nextLine(); // User will write Name.

		System.out.println("Enter your Age");
		Age = sc.nextInt(); // User will write Age

		// Check if Age is greater than or equal to 18 to open an account
		if (Age >= 18) {
			System.out.println("Valid Age");
			this.checkno();
		} else {
			System.out.println("Invalid Age");
			System.out.println("Age must be above 18");
			count++;
			this.signin();
		}
	}

	// Mobile no method
	void checkno() throws Exception {
		System.out.println("Enter mobile no");

		// Consume newline character if count is greater than or equal to 0
		if (count >= 0)
			sc.nextLine();

		MobileNo = sc.nextLine(); // User will write mobile no

		// Check if the mobile number is exactly 10 digits long
		if (MobileNo.length() != 10) {
			System.out.println("Invalid mobile number! Please enter a 10-digit mobile number. Starting with 1");
			count++;
			this.checkno();
		}

		// Check if the first digit is 1
		if (MobileNo.charAt(0) != '1') {
			System.out.println("Invalid mobile number! The first digit should be 1.");
			count++;
			this.checkno();
		}

		this.ssn();
	}

	// Social Security Number (SSN) method
	public void ssn() throws Exception {
		System.out.println("Enter your social security no like eg.AAABBBCCC");

		// Consume newline character if count is greater than or equal to 1
		if (count >= 1)
			sc.nextLine();

		Ssn = sc.nextLine();

		// Check if the entered SSN is valid
		if (isValidString(Ssn)) {
			System.out.println("Valid.");
			this.Pin(); // Proceed to set PIN
		} else {
			System.out.println("The input SSN is not valid.");
			System.out.println("Please enter SSN like eg.YYYDDDLLL");
			this.ssn(); // Retry if SSN is not valid
		}
	}

	// Method to check if a string is valid
	public static boolean isValidString(String input) {
		// Check if the string has exactly 9 characters
		if (input.length() != 9) {
			return false;
		}

		// Check if the string contains only non-digit, non-special characters
		for (char c : input.toCharArray()) {
			if (Character.isDigit(c) || !Character.isLetterOrDigit(c)) {
				return false;
			}
		}

		return true;
	}

	// PIN (Personal Identification Number) method
	void Pin() throws Exception {
		System.out.println("Your Account no is:" + Account_no); // Account no will be given by bank
		System.out.println("Set your PIN");
		System.out.println("PIN should be of exact 4 digits");

		Pin = sc.nextInt(); // User will enter PIN with the conditions below

		// Validate the entered PIN
		if (this.validate(Pin)) {
			System.out.println("Enter your starting Balance you want to deposit");
			Balance = sc.nextDouble(); // User will enter Balance

			this.ownerset(); // If the PIN is correct, proceed to the login method
		} else {
			System.out.println("1. PIN should be of 4 digits");
			this.Pin(); // Retry if PIN is not valid
		}
	}

	// Method to validate a PIN
	boolean validate(int Pin) {
		// Check the length of the PIN
		if (Integer.toString(Pin).length() != 4) {
			System.out.println("Invalid PIN");
			return false;
		}

		return true; // PIN is valid
	}

	// Method To set Owner
	void ownerset() throws Exception {
		profile();
		setOwner();
		login();
	}

	// Login method
	void login() throws Exception {
		System.out.println("----------Login----------");
		System.out.println("Enter your Account no or 0 for exit ");

		Acc_no = sc.nextInt(); // User will enter the given account no

		if (Acc_no == 0) {
			System.out.println("------------------" +
					"Thanks For Using Our Appilcation--------------------- ");
			System.exit(0); // Exit the program
		} else {
			if (Acc_no == Account_no) { // Entered account no should match the given account no
				this.Epin(); // Proceed to enter PIN
			} else {
				System.out.println("Enter valid Account no");
				this.login(); // Retry if the entered account no is not valid
			}
		}
	}

	// Method to check entered PIN is true
	void Epin() throws Exception {
		System.out.println("Enter your PIN");

		Epin = sc.nextInt(); // User will enter the PIN which they have set

		if (Epin == Pin) { // Entered PIN should match the one they have set
			this.menu(); // Proceed to the main menu
		} else {
			System.out.println("Enter valid PIN");
			this.Epin(); // Retry if the entered PIN is not valid
		}
	}

	// Method to perform operations
	void menu() throws Exception {
		System.out.println();
		System.out.println("1.  Check Balance");
		System.out.println("2.  Make a Fixed Deposit");
		System.out.println("3.  Request to Bank");
		System.out.println("4.  Transfer Money");
		System.out.println("5.  Passbook");
		System.out.println("6.  Close Account");
		System.out.println("7.  View Transaction History");
		System.out.println("8.  Logout");
		System.out.println("Enter your choice");

		int option = sc.nextInt(); // User will enter the option they would like to perform

		// Perform actions based on the user's choice
		if (option == 1) {
			checkbalance();
		} else if (option == 2) {
			fixdeposit();
		} else if (option == 3) {
			Request();
		} else if (option == 4) {
			transfermoney();
		} else if (option == 5) {
			print(Account_no);
		} else if (option == 6) {
			closeAccount();
		} else if (option == 7) {
			viewTransactionHistory();
		} else if (option == 8) {
			System.out
					.println("---------------" + "\"Thank you for using our bank application\"" + "-----------------");
			login();
		} else {
			System.out.println("Enter valid choice");
			menu(); // If an invalid choice is entered, loop back to the menu
		}
	}

	// Method to check balance
	void checkbalance() throws Exception {
		try {
			String squery = "Update owner_details set Balance = ? WHERE AccountNo = ? ";
			try (PreparedStatement pst = con.prepareStatement(squery)) {
				pst.setDouble(1, Balance);
				pst.setInt(2, Account_no);
			}
			// SQL query to retrieve the balance from the 'owner_details' table
			String query = "SELECT Balance FROM owner_details WHERE AccountNo = ?";
			try (PreparedStatement pst = con.prepareStatement(query)) {
				pst.setInt(1, Account_no);

				try (ResultSet resultSet = pst.executeQuery()) {
					if (resultSet.next()) {
						// Retrieve the balance from the result set
						double balance = resultSet.getDouble("Balance");
						balance = Balance;
						System.out.println("Current Balance: " + balance + "$");
					} else {
						System.out.println("Account not found.");
					}
				}
			}
		} catch (SQLException e) {
			System.err.println("Error checking balance: " + e.getMessage());
		}

		this.menu(); // Return to the main menu
	}

	// Method to Make Fix Deposit
	void fixdeposit() throws Exception {
		// Prompt the user to enter the amount they want to fix deposit
		System.out.println("Enter Money you want to fix deposit");
		Money = sc.nextDouble(); // user Money which they would like to keep in fixed deposit

		// Prompt the user to enter the number of years for the fixed deposit
		System.out.println("Enter years you want to make a fixed deposit");
		Year = sc.nextInt(); // user will enter years they would like to keep Money

		// Check if the user has sufficient balance for the fixed deposit
		if (Money > Balance) {
			System.out.println("Balance is not sufficient");
			this.menu(); // Redirect the user to the main menu if the balance is insufficient
		} else {
			// Deduct the fixed deposit amount from the user's balance
			Balance = Balance - Money;
			// Update the balance in the accounts map using the account number as the key
			accounts.put(Account_no, Balance);

			// Display a success message for the fixed deposit
			System.out.println("Fixed Deposit is successfully done For " + Year + " Years");

			// Calculate simple interest for the fixed deposit using the formula
			Si = (Money * 0.85 * Year);
			// Calculate the total amount after interest
			Result = Si + Money;

			// Display the total amount after the specified number of years
			System.out.println("Your Money after " + Year + " years will be: " + Result + "$");

			// Log the fixed deposit details to a database connection (con)
			logFixedDeposit(con, Account_no, Money, Year);

			// Write the fixed deposit details to the passbook
			writeToPassBook(Account_no, MobileNo, Sacc_no, Money, null);

			// Display the updated balance after fixing the money
			this.checkbalance();
		}
	}

	// Method to handle user requests
	void Request() throws Exception {
		// Display a menu of options for the user to choose from
		System.out.println("1. Request for More Help");
		System.out.println("2. Request for Bank Statement");
		System.out.println("3. Request for New Credit Card");
		System.out.println("4. Display owner details.");
		System.out.println("5. Update your details given to the bank");
		System.out.println("6. Change pin.");
		System.out.println("7. Exit");
		System.out.println("Enter your choice");

		// User enters the option they would like to perform
		int Option = sc.nextInt();

		// Switch statement for selecting the category based on the user's choice
		switch (Option) {
			case 1:
				// Display contact information for further help
				System.out.println(
						"For further help, you can contact on +111-222-3330 \n Or on our Email id: chicagobank@gmail.com");
				break;
			case 2:
				// Acknowledge the acceptance of the bank statement request
				System.out.println("Your Request has been Accepted");
				break;
			case 3:
				// Check if the user has sufficient balance for a new credit card
				if (Balance < 100) {
					System.out.println("You have insufficient Money for applying for a new credit card");
				} else {
					// Acknowledge the acceptance of the credit card request
					System.out.println("Your Request has been Accepted");
					System.out.println("Your new Credit card will be delivered soon to your house.");
				}
				break;
			case 4:
				// Display owner details
				displayAccountDetails();
				break;
			case 5:
				// Update owner details
				updateownerDetails();
				break;
			case 6:
				// Change pin
				changePin();
				break;
			case 7:
				// Return to the main menu
				menu();
				break;
			default:
				System.out.println("Invalid Input");
		}

		// Return to the main menu after processing the user's request
		this.menu();
	}

	// Method to Transfer Money
	void transfermoney() throws Exception {
		// Prompt the user to enter the account number to which they want to transfer
		// money
		System.out.println("Enter Account no to which you want to transfer Money");
		Sacc_no = sc.nextInt(); // user will enter account no they want to transfer Money

		// Check if the user is trying to transfer money to the same account
		if (Sacc_no == Account_no) {
			System.out.println("You can't transfer Money to the same Account");
			transfermoney();
		} else {
			// Prompt the user to enter their PIN for security
			System.out.println("Enter your Pin");
			Tpin = sc.nextInt(); // User will enter the password for security

			// Check if the entered PIN is correct
			if (Tpin == Pin) {
				// Prompt the user to enter the amount of money they want to transfer
				System.out.println("Enter the amount of Money you want to transfer");
				Money = sc.nextDouble(); // user will enter the amount of Money they want to transfer

				// Check if the entered money is less than or equal to the balance
				if (Money >= Balance) {
					System.out.println("Balance is not sufficient");
					this.menu();
				} else {
					// Deduct the transferred amount from the user's balance
					Balance = Balance - Money;
					// Update the balance in the accounts map using the account number as the key
					accounts.put(Account_no, Balance);

					// Display success message for the transfer
					System.out.println("Successfully Transferred");
					System.out.println("Money is successfully transferred to " + Sacc_no);

					// Log the transaction in the passbook
					logTransaction(con, Account_no, Money, Balance, Sacc_no, "Withdraw");
					writeToPassBook(Account_no, "Transfer", Sacc_no, Money, accounts.get(Account_no));

					// showing the user Balance after transferring Money
					this.checkbalance();
				}
			} else {
				// Invalid PIN entered, prompt user to enter a valid PIN
				System.out.println("Enter a valid PIN");
				this.transfermoney();
			}
		}
	}

	// Method to display user profile and write it to a file
	void profile() throws Exception {
		// Convert Account_no and Age to strings for writing to the file
		String accountNumber = Integer.toString(Account_no);
		String sage = Integer.toString(Age);

		// Write user profile information to a file
		writer.write("Name :" + Name);
		writer.newLine();
		writer.write("Age : " + sage);
		writer.newLine();
		writer.write("Mobile no :" + MobileNo);
		writer.newLine();
		writer.write("Account no :" + accountNumber);
		writer.newLine();
		writer.newLine();
		writer.write("Transactions:--");
		writer.newLine();
		writer.newLine();

		// Flush the writer to ensure data is written to the file
		writer.flush();

	}

	// Method to write transaction details to the passbook
	void writeToPassBook(int Account_no, String Msg, int Sacc_no, double Money, Double Balance)
			throws Exception {
		// Convert source account number to string
		String sourceAccount = Integer.toString(Sacc_no);
		String transactionType = "";

		// Determine the transaction type based on the message (Msg)
		if (Msg.equals("Withdrawed")) {
			transactionType = "Withdrawal";
		} else if (Msg.equals("Deposited")) {
			transactionType = "Deposit";
		} else if (Msg.equals("FixedDeposit")) {
			transactionType = "Fixed Deposit";
		} else if (Msg.equals("Transfer")) {
			transactionType = "Transfer to Account " + sourceAccount;
		}

		// Get the current date and format it
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String formattedDate = dateFormat.format(date);

		// Write transaction details to the passbook
		writer.write("Date: " + formattedDate);
		System.out.println(sc.nextLine());
		writer.newLine();
		writer.write("Transaction Type: " + transactionType);
		writer.newLine();
		writer.write("Amount: $" + Money);
		writer.newLine();
		writer.write("Balance: $" + Balance);
		writer.newLine();
		writer.newLine(); // Add a blank line to separate entries

		// Flush the writer to ensure data is written to the file
		writer.flush();
		// Return to the main menu
		menu();
	}

	// Method to print user details from a file
	void print(int Account_no) throws Exception {
		// Create a BufferedReader to read data from the file
		BufferedReader reader = new BufferedReader(new FileReader(Account_no + ".txt"));
		String line = reader.readLine();

		// Loop through each line in the file and print it
		while (line != null) {
			System.out.println(line);
			line = reader.readLine();
		}

		// Close the reader
		reader.close();
		// Return to the main menu
		menu();
	}

	// Method to log transaction in the database
	void logTransaction(Connection con, int Account_no, double Money, double balance, int sacc_no, String string)
			throws Exception {
		// Get the current date and time
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		String formattedDate = dateFormat.format(date);

		// SQL query to insert transaction details into the 'transactions' table
		String insertQuery = "INSERT INTO transactions (account_id, transaction_type, Money, date) VALUES (?, ?, ?, ?)";
		try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
			// Set values for the prepared statement
			pst.setInt(1, Account_no);
			pst.setString(2, string); // Use string for transaction_type
			pst.setDouble(3, Money);
			pst.setString(4, formattedDate); // Set the current date

			// Execute the SQL update and check if it was successful
			int r = pst.executeUpdate();
			if (r > 0) {
				System.out.println("Transaction logged successfully.");
			} else {
				System.out.println("Failed to log transaction.");
			}
		}
	}

	// Method to update balance in the database for fixed deposit
	void fixDepositt(Connection con, int Account_no, double Money, int Year, int bacc_no2, int year2) throws Exception {
		try {
			// SQL query to update balance in the 'bank' table for fixed deposit
			String updateQuery = "UPDATE bank SET Balance = Balance - ? WHERE AccountNo = ?";
			try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
				// Set values for the prepared statement
				pst.setDouble(1, Money);
				pst.setInt(2, Account_no);

				// Execute the SQL update and check if it was successful
				int r = pst.executeUpdate();
				if (r > 0) {
					System.out.println("Balance updated successfully.");
				} else {
					System.out.println("Failed to update balance.");
				}
			}
		} catch (SQLException e) {
			System.err.println("SQL Error: " + e.getMessage());
		}
		// Log the fixed deposit transaction
		logTransaction(con, Account_no, Money, Balance, Sacc_no, "FixedDeposit");
		// Recursive call to fixDepositt (probably a mistake, please review this)
		fixDepositt(con, Account_no, Money, Year, Account_no, Year);
	}

	// Method to log fixed deposit details in the database
	void logFixedDeposit(Connection con, int Account_no, double Money, int Years) throws Exception {
		// SQL query to insert fixed deposit details into the 'fixed_deposits' table
		String insertQuery = "INSERT INTO fixed_deposits (account_id, deposit_amount, years) VALUES (?, ?, ?)";
		try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
			// Set values for the prepared statement
			pst.setInt(1, Account_no);
			pst.setDouble(2, Money);
			pst.setInt(3, Years);

			// Execute the SQL update and check if it was successful
			int r = pst.executeUpdate();
			if (r > 0) {
				System.out.println("Fixed Deposit logged successfully");
			} else {
				System.out.println("Failed to log Fixed Deposit");
			}
		}
	}

	// Method to insert owner details into the 'owner_details' table
	void setOwner() throws Exception {
		try {
			// SQL query to insert owner details into the 'owner_details' table
			String insertQuery = "INSERT INTO owner_details (AccountNo, Name, Age, Mobileno, Ssn,Balance) VALUES (?, ?, ?, ?, ?,?)";
			try (PreparedStatement pst = con.prepareStatement(insertQuery)) {
				// Set values for the prepared statement
				pst.setInt(1, Account_no);
				pst.setString(2, Name);
				pst.setInt(3, Age);
				pst.setString(4, MobileNo);
				pst.setString(5, Ssn);
				pst.setDouble(6, Balance);

				// Execute the SQL update and check if it was successful
				int rowsAffected = pst.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println("Owner details inserted successfully. Account number: " + Account_no);
				} else {
					System.out.println("Failed to insert owner details.");
				}
			} catch (SQLException e) {
				System.err.println("SQL Error: " + e.getMessage());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// Method to update owner details
	void updateownerDetails() throws Exception {
		// Prompt the user to enter their account number
		System.out.println("Enter your Account no");
		int dacc_no = sc.nextInt();
		sc.nextLine(); // Consume the newline character left by nextInt()

		// Check if the entered account number matches the current account number
		if (dacc_no == Account_no) {
			// Prompt the user to choose the field to update
			System.out.println("For updating Press \n 1. For Name \n 2. For Mobile number \n 3. For SSN");
			int ch = sc.nextInt();
			sc.nextLine(); // Consume the newline character left by nextInt()

			// Check the user's choice and update the corresponding field
			if (ch == 1) {
				System.out.println("Enter Your new Name");
				String uName = sc.nextLine();
				String updateQuery = "UPDATE owner_details SET Name = ? WHERE AccountNo = ?";
				try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
					pst.setString(1, uName);
					pst.setInt(2, dacc_no);
					Name = uName; // Update the Name field in the class
					pst.executeUpdate();
					System.out.println("Name updated successfully.");
				}
			} else if (ch == 2) {
				System.out.println("Enter Your new Mobile number");
				String uMobileno = sc.nextLine();
				String updateQuery = "UPDATE owner_details SET Mobileno = ? WHERE AccountNo = ?";
				try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
					pst.setString(1, uMobileno);
					pst.setInt(2, dacc_no);
					MobileNo = uMobileno; // Update the MobileNo field in the class
					pst.executeUpdate();
					System.out.println("Mobile number updated successfully.");
				}
			} else if (ch == 3) {
				System.out.println("Enter Your new SSN");
				String uSsn = sc.nextLine();
				String updateQuery = "UPDATE owner_details SET Ssn = ? WHERE AccountNo = ?";
				try (PreparedStatement pst = con.prepareStatement(updateQuery)) {
					pst.setString(1, uSsn);
					pst.setInt(2, dacc_no);
					Ssn = uSsn; // Update the Ssn field in the class
					pst.executeUpdate();
					System.out.println("SSN updated successfully.");
				}
			} else {
				System.out.println("Enter a valid input.");
			}
		} else {
			System.out.println("Account No does not exist");
		}
		this.menu();
	}

	// Method to display account details
	void displayAccountDetails() throws Exception {
		System.out.println("Account Details:");
		System.out.println("Name: " + Name);
		System.out.println("Age: " + Age);
		System.out.println("Mobile Number: " + MobileNo);
		System.out.println("SSN: " + Ssn);
		System.out.println("Account Number: " + Account_no);
		System.out.println("Balance: $" + Balance);
		this.menu(); // Return to the main menu
	}

	// Method to change PIN
	void changePin() throws Exception {
		System.out.println("Enter your current PIN");
		int currentPin = sc.nextInt();

		// Check if the entered current PIN matches the actual PIN
		if (currentPin == Pin) {
			System.out.println("Enter your new PIN");
			int newPin = sc.nextInt();

			// Validate the new PIN format
			if (validate(newPin)) {
				Pin = newPin; // Update the PIN
				System.out.println("PIN changed successfully");
				this.menu(); // Return to the main menu
			} else {
				System.out.println("Invalid PIN. Pin should be of 4 digits.");
				changePin(); // Retry if the new PIN is invalid
			}
		} else {
			System.out.println("Incorrect current PIN. Please try again.");
			changePin(); // Retry if the current PIN is incorrect
		}
	}

	// Method to close account
	void closeAccount() throws Exception {
		System.out.println("Are you sure you want to close your account? (yes/no)");
		sc.nextLine(); // Consume the newline character left by previous input
		String confirmation = sc.nextLine().toLowerCase();

		if (confirmation.equals("yes")) {
			// Implement code to delete account details from the database
			String deleteQuery = "DELETE FROM owner_details WHERE AccountNo = ?";
			try (PreparedStatement pst = con.prepareStatement(deleteQuery)) {
				pst.setInt(1, Account_no);
				int rowsAffected = pst.executeUpdate();

				if (rowsAffected > 0) {
					System.out.println("Account closed successfully.");
					System.exit(0); // Exit the program after closing the account
				} else {
					System.out.println("Failed to close account.");
					this.menu(); // Return to the main menu if account closure fails
				}
			}
		} else if (confirmation.equals("no")) {
			System.out.println("Account closure cancelled.");
			this.menu(); // Return to the main menu if account closure is cancelled
		} else {
			System.out.println("Invalid input. Please enter 'yes' or 'no'.");
			closeAccount(); // Retry if the input is invalid
		}
	}

	// Method to view transaction history
	void viewTransactionHistory() throws Exception {
		try {
			// SQL query to retrieve transaction records for the current account
			String query = "SELECT * FROM transactions WHERE account_id = ?";

			try (PreparedStatement pst = con.prepareStatement(query)) {
				pst.setInt(1, Account_no);

				try (ResultSet resultSet = pst.executeQuery()) {
					System.out.println("Transaction History:");

					// Iterate through the result set and print transaction details
					while (resultSet.next()) {
						int transactionId = resultSet.getInt("transaction_id");
						String transactionType = resultSet.getString("transaction_type");
						double amount = resultSet.getDouble("Money");
						// Assuming 'date' is a column in your 'transactions' table
						String date = resultSet.getString("date");

						System.out.println("Transaction ID: " + transactionId);
						System.out.println("Date: " + date);
						System.out.println("Transaction Type: " + transactionType);
						System.out.println("Amount: $" + amount);

						System.out.println("---------------------------");
					}

				}
			}
		} catch (SQLException e) {
			System.err.println("Error fetching transaction history: " + e.getMessage());
		}

		this.menu(); // Return to the main menu
	}

	public static void main(String[] args) throws Exception {

		// Database connection details
		Class.forName("com.mysql.cj.jdbc.Driver");
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/Bank", "root", "");
		st = con.createStatement();

		// Create the 'transactions' table
		String transactionsTableSQL = "CREATE TABLE IF NOT EXISTS transactions (" +
				"transaction_id INT AUTO_INCREMENT PRIMARY KEY, " +
				"account_id INT, " +
				"transaction_type VARCHAR(30), " +
				"Money DOUBLE, " +
				"date TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
		st.executeUpdate(transactionsTableSQL);

		// Create the 'fixed_deposits' table
		String fixedDepositsTableSQL = "CREATE TABLE IF NOT EXISTS fixed_deposits (" +
				"deposit_id INT AUTO_INCREMENT PRIMARY KEY," +
				"account_id INT,deposit_amount DOUBLE,years INT)";
		st.executeUpdate(fixedDepositsTableSQL);

		// Create the 'owner_details' table
		String ownerDetails = "CREATE TABLE IF NOT EXISTS owner_details (" +
				"AccountNo INT,Name VARCHAR(20), Balance DOUBLE,Age int," +
				"Mobileno VARCHAR(20),Ssn VARCHAR(20))";
		st.executeUpdate(ownerDetails);

		// Check if the database connection is successful
		if (con != null) {
			System.out.println("---------------" + "\"Welcome to Bank of Chicago, Illinois\"" + "-----------------");
		} else {
			System.out.println("Server not connected");
		}

		// Create an instance of the Bankjdbc class
		Bankjdbc obj = new Bankjdbc();
		// Call the enter() method, presumably to start the user interaction
		obj.enter();

	}
}