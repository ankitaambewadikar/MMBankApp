package com.moneymoney.account.service;

import java.sql.SQLException;
import java.util.List;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.exception.AccountNotFoundException;

public interface SavingsAccountService {

	SavingsAccount createNewAccount(String accountHolderName, double accountBalance, boolean salary) throws ClassNotFoundException, SQLException;

	SavingsAccount updateAccount(SavingsAccount account) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount deleteAccount(int accountNumber) throws ClassNotFoundException, AccountNotFoundException, SQLException;
	
	List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException;
	
	double checkCurrentBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	void fundTransfer(SavingsAccount sender, SavingsAccount receiver, double amount) throws ClassNotFoundException, SQLException;
	void deposit(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException;
	void withdraw(SavingsAccount account, double amount) throws ClassNotFoundException, SQLException;

	//SavingsAccount updateAccountByIsSalaried(int accountNumber, boolean newIssalaried) throws ClassNotFoundException, SQLException, AccountNotFoundException;

	SavingsAccount getAccountByName(String accountHolderName) throws ClassNotFoundException, AccountNotFoundException, SQLException;

	List<SavingsAccount> getAllAccountsBetweenSalaryRange(double minimumRange, double maximumRange)
			throws SQLException, ClassNotFoundException;
	
	List<SavingsAccount> sortAccording() throws ClassNotFoundException, SQLException;
}











