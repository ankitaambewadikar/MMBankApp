package com.moneymoney.account.ui;

import java.io.ObjectInputStream.GetField;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.service.SavingsAccountService;
import com.moneymoney.account.service.SavingsAccountServiceImpl;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;
import com.mysql.fabric.xmlrpc.base.Array;

public class AccountCUI {
	private static Scanner scanner = new Scanner(System.in);
	private static SavingsAccountService savingsAccountService = new SavingsAccountServiceImpl();

	public static void start() {

		do {
			System.out.println("****** Welcome to Money Money Bank********");
			System.out.println("1. Open New Savings Account");
			System.out.println("2. Update Account");
			System.out.println("3. Close Account");
			System.out.println("4. Search Account");
			System.out.println("5. Withdraw");
			System.out.println("6. Deposit");
			System.out.println("7. FundTransfer");
			System.out.println("8. Check Current Balance");
			System.out.println("9. Get All Savings Account Details");
			System.out.println("10. Sort Accounts");
			System.out.println("11. Exit");
			System.out.println();
			System.out.println("Make your choice: ");

			int choice = scanner.nextInt();

			performOperation(choice);

		} while (true);
	}

	private static void performOperation(int choice) {
		switch (choice) {
		case 1:
			acceptInput("SA");
			break;
		case 9:
			showAllAccounts();
			break;
		case 5:
			withdraw();
			break;
		case 6:
			deposit();
			break;
		case 7:
			fundTransfer();
			break;
		case 3:
			closeAccount();
			break;
		case 8:
			currentBalance();
			break;
		case 4:
			searchAccount();
			break;
		case 2:
			updateAccountDetails();
			break;
		case 10:
			sortMenu();
			break;
		case 11:
			try {
				DBUtil.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.exit(0);
			break;
		default:
			System.err.println("Invalid Choice!");
			break;
		}

	}

	private static void fundTransfer() {
		System.out.println("Enter Account Sender's Number: ");
		int senderAccountNumber = scanner.nextInt();
		System.out.println("Enter Account Receiver's Number: ");
		int receiverAccountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		try {
			SavingsAccount senderSavingsAccount = savingsAccountService
					.getAccountById(senderAccountNumber);
			SavingsAccount receiverSavingsAccount = savingsAccountService
					.getAccountById(receiverAccountNumber);
			savingsAccountService.fundTransfer(senderSavingsAccount,
					receiverSavingsAccount, amount);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void deposit() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.deposit(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}

	private static void withdraw() {
		System.out.println("Enter Account Number: ");
		int accountNumber = scanner.nextInt();
		System.out.println("Enter Amount: ");
		double amount = scanner.nextDouble();
		SavingsAccount savingsAccount = null;
		try {
			savingsAccount = savingsAccountService
					.getAccountById(accountNumber);
			savingsAccountService.withdraw(savingsAccount, amount);
			DBUtil.commit();
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (Exception e) {
			try {
				DBUtil.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}
	}


	private static void showAllAccounts() {
		List<SavingsAccount> savingsAccounts;
		try {
			savingsAccounts = savingsAccountService.getAllSavingsAccount();
			for (SavingsAccount savingsAccount : savingsAccounts) {
				System.out.println(savingsAccount);
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}

	private static void acceptInput(String type) {
		if (type.equalsIgnoreCase("SA")) {
			System.out.println("Enter your Full Name: ");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			System.out
					.println("Enter Initial Balance(type na for Zero Balance): ");
			String accountBalanceStr = scanner.next();
			double accountBalance = 0.0;
			if (!accountBalanceStr.equalsIgnoreCase("na")) {
				accountBalance = Double.parseDouble(accountBalanceStr);
			}
			System.out.println("Salaried?(y/n): ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			createSavingsAccount(accountHolderName, accountBalance, salary);
		}
	}

	private static void createSavingsAccount(String accountHolderName,
			double accountBalance, boolean salary) {
		try {
			System.out.println(savingsAccountService.createNewAccount(
					accountHolderName, accountBalance, salary));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void closeAccount() {
		System.out.println("Enter the Account Number to be closes: ");
		int closeAccountNumber = scanner.nextInt();
		try {
			SavingsAccount closeSavingAccount = savingsAccountService
					.deleteAccount(closeAccountNumber);
			DBUtil.commit();
		} catch (ClassNotFoundException | AccountNotFoundException
				| SQLException e) {
			e.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		}

	}

	private static void currentBalance() {
		System.out
				.println("Enter the Account number of which current Balance to be checked :");
		int checkAccountNumberBalance = scanner.nextInt();
		try {
			double checkCurrentBalanceOfAccountNumber = savingsAccountService
					.checkCurrentBalance(checkAccountNumberBalance);
			System.out.println("current Balance is: "
					+ checkCurrentBalanceOfAccountNumber);
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e) {
			e.printStackTrace();
		} catch (Exception exp) {
			exp.printStackTrace();
		}
	}

	private static void updateAccountDetails() {
		SavingsAccount account=null;
		System.out.println("Enter account Number of which details to be updated:");
		int accountNumber = scanner.nextInt();
		try {
			account = savingsAccountService.getAccountById(accountNumber);
		} catch (ClassNotFoundException | SQLException
				| AccountNotFoundException e1) {
			e1.printStackTrace();
		}
		System.out.println("Which of following details to update?");
		System.out.println("1. Account Holder Name");
		System.out.println("2. Salried/Non-Salried Account");
		System.out.println("3 Update both ");
		System.out.println("Make your choice :");
		int updateChoice = scanner.nextInt();
		switch (updateChoice) {

		case 1: {
			System.out.println("Enter new Account holder name:");
			String newAccountHolderName = scanner.nextLine();
			newAccountHolderName = scanner.nextLine();
			account.getBankAccount().setAccountHolderName(newAccountHolderName);
			SavingsAccount updateByName;
			try {
				updateByName = savingsAccountService.updateAccount(account);
				System.out.println(updateByName);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			
		}break;
		case 2: {
			System.out.println("Enter y/n to change the Salaried/non-Salaried Account:  ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			account.setSalary(salary);
			try {
				SavingsAccount updateSalary = savingsAccountService.updateAccount(account);
				System.out.println(updateSalary);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			
		}
			break;
		case 3:{
			System.out.println("Enter new Account holder name:");
			String newAccountHolderName = scanner.nextLine();
			System.out.println("Enter y/n to change the Salaried/non-Salaried Account:  ");
			boolean salary = scanner.next().equalsIgnoreCase("n") ? false
					: true;
			account.getBankAccount().setAccountHolderName(newAccountHolderName);
			account.setSalary(salary);
			SavingsAccount both;
			try {
				both = savingsAccountService.updateAccount(account);
				System.out.println(both);
			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
			
			
		}break;
		default:
			System.err.println("Invalid Choice!");
			break;

		}
	}

	private static void searchAccount() {
		System.out.println("Search Account By:");
		System.out.println("1 By AccountId ");
		System.out.println("2 By Account Holder Name");
		System.out.println("3 By Balance Range");
		System.out.println("Make your Choice: ");

		int searchChoice = scanner.nextInt();
		performSearchAccountOperation(searchChoice);

	}

	private static void performSearchAccountOperation(int searchChoice) {
		switch (searchChoice) {

		case 1: {
			System.out.println("Enter Account Number: ");
			int accountNumber = scanner.nextInt();
			try {
				SavingsAccount savingsAccount = savingsAccountService
						.getAccountById(accountNumber);
				System.out.println(savingsAccount);

			} catch (ClassNotFoundException | SQLException
					| AccountNotFoundException e) {
				e.printStackTrace();
			}
		}
			break;
		case 2: {
			System.out.println("Enter Account Holder name: ");
			String accountHolderName = scanner.nextLine();
			accountHolderName = scanner.nextLine();
			try {
				SavingsAccount savingAccount = savingsAccountService
						.getAccountByName(accountHolderName);
				System.out.println(savingAccount);
			} catch (ClassNotFoundException | AccountNotFoundException
					| SQLException e) {
				e.printStackTrace();
			}
		}
			break;
		case 3: {
			System.out.println("Enter the AmountBalance Range to seacrch Account:");
			System.out.println("Enter minimum Range:");
			double minimumRange = scanner.nextDouble();
			System.out.println("Enter maximum Range:");
			double maximumRange = scanner.nextDouble();
			System.out.println("in");
			try {
				List<SavingsAccount> savingsAccounts = savingsAccountService.getAllAccountsBetweenSalaryRange(minimumRange,
								maximumRange);
				for (SavingsAccount savings : savingsAccounts) {
					System.out.println(savings);
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
			break;
		default:
			System.err.println("Invalid Choice!");
			break;

		}

	}

	private static void sortMenu() {
		int choice = 0;
		do {
			System.out.println("+++++Ways of Sorting+++++++");
			System.out.println("1. Account Holder Name");
			System.out.println("2. Account Balance");
			System.out.println("3. Exit from Sorting");

			choice = scanner.nextInt();
			performSort(choice);
		} while (true);
	}

	private static void performSort(int choice) {
		
		switch (choice) {
		case 1:{
			System.out.println("1 Ascending AccountholderName");
			System.out.println("2 Descending  AccountholderName");
			System.out.println("Make choice");
			int choiceOne = scanner.nextInt();
			performAscendingSort(choiceOne);
		}
	}

	
	}

	private static void performAscendingSort(int choiceOne) {
		
		//savingsAccountService.sortAccording(accountHolderName);
	}
}
