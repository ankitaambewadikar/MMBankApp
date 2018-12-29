package com.moneymoney.account.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.PreAction;

import com.moneymoney.account.SavingsAccount;
import com.moneymoney.account.util.DBUtil;
import com.moneymoney.exception.AccountNotFoundException;

public class SavingsAccountDAOImpl implements SavingsAccountDAO {

	public SavingsAccount createNewAccount(SavingsAccount account) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOUNT VALUES(?,?,?,?,?,?)");
		preparedStatement.setInt(1, account.getBankAccount().getAccountNumber());
		preparedStatement.setString(2, account.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(3, account.getBankAccount().getAccountBalance());
		preparedStatement.setBoolean(4, account.isSalary());
		preparedStatement.setObject(5, null);
		preparedStatement.setString(6, "SA");
		preparedStatement.executeUpdate();
		preparedStatement.close();
		DBUtil.commit();
		return account;
	}

	public List<SavingsAccount> getAllSavingsAccount() throws ClassNotFoundException, SQLException {
		List<SavingsAccount> savingsAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM ACCOUNT");
		while (resultSet.next()) {// Check if row(s) is present in table
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
		DBUtil.commit();
		return savingsAccounts;
	}
	@Override
	public void updateBalance(int accountNumber, double currentBalance) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		connection.setAutoCommit(false);
		PreparedStatement preparedStatement = connection.prepareStatement
				("UPDATE ACCOUNT SET account_bal=? where account_id=?");
		preparedStatement.setDouble(1, currentBalance);
		preparedStatement.setInt(2, accountNumber);
		preparedStatement.executeUpdate();
	}
	
	@Override
	public SavingsAccount getAccountById(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account where account_id=?");
		preparedStatement.setInt(1, accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount = null;
		if(resultSet.next()) {
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			return savingsAccount;
			
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}
	

	@Override
	public SavingsAccount getAccountByName(String accountHolderName) throws ClassNotFoundException, SQLException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from account WHERE account_hn=?");
		preparedStatement.setString(1, accountHolderName);
		ResultSet resultSet = preparedStatement.executeQuery();
		SavingsAccount savingsAccount= null;
		if(resultSet.next())
		{
			int accountNumber = resultSet.getInt("account_Id");
			double accountBalance = resultSet.getDouble("account_balance");
			boolean salary = resultSet.getBoolean("salary");
			savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
		}
		return savingsAccount;
	}
	

	@Override
	public SavingsAccount deleteAccount(int accountNumber) throws SQLException, ClassNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("DELETE FROM account where account_id=?");
		preparedStatement.setInt(1, accountNumber);
		boolean resultSet = preparedStatement.execute();
		SavingsAccount savingAccount = null;
		return savingAccount;
	
	}

	@Override
	public double checkCurrentBalance(int accountNumber) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
				("SELECT * FROM account WHERE account_Id=?");
		preparedStatement.setInt(1,accountNumber);
		ResultSet resultSet = preparedStatement.executeQuery();
		if(resultSet.next()){
			double accountBalance = resultSet.getDouble(3);
				return accountBalance;
		}

		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}

	@Override
	public SavingsAccount updateAccount(SavingsAccount account) throws SQLException, ClassNotFoundException, AccountNotFoundException {
		
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ACCOUNT SET account_hn=?,salary=? WHERE account_Id = ?");
		preparedStatement.setInt(3,account.getBankAccount().getAccountNumber());
		preparedStatement.setBoolean(2,account.isSalary());
		preparedStatement.setString(1,account.getBankAccount().getAccountHolderName());
		preparedStatement.executeUpdate();
		DBUtil.commit();
		return getAccountById(account.getBankAccount().getAccountNumber());
	
	}

	/*@Override
	public SavingsAccount updateAccountByIsSalaried(int accountNumber,
			boolean newIssalaried) throws ClassNotFoundException, SQLException, AccountNotFoundException {
		Connection connection = DBUtil.getConnection();
		if(getAccountById(accountNumber).getBankAccount().getAccountNumber() == accountNumber){
			PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ACCOUNT SET salary=? WHERE account_Id = ?");
			preparedStatement.setInt(2,accountNumber);
			preparedStatement.setBoolean(1,newIssalaried);
			preparedStatement.executeUpdate();
			DBUtil.commit();
			return getAccountById(accountNumber);
		}
		throw new AccountNotFoundException("Account with account number "+accountNumber+" does not exist.");
	}*/

	@Override
	public List<SavingsAccount> getAllAccountsBetweenSalaryRange(double minimumRange,double maximumRange) throws SQLException, ClassNotFoundException {

		List<SavingsAccount> savingsAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
		("SELECT * FROM ACCOUNT WHERE account_balance BETWEEN ? AND ?");
		preparedStatement.setDouble(1, minimumRange);
		preparedStatement.setDouble(2, maximumRange);
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			savingsAccounts.add(savingsAccount);
		}
		return savingsAccounts;
		
	}

	@Override
	public List<SavingsAccount> sortByAscending(SavingsAccount savingAccount) throws SQLException, ClassNotFoundException, AccountNotFoundException {
		List<SavingsAccount> sortAccounts = new ArrayList<>();
		Connection connection = DBUtil.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement
		("SELECT * FROM ACCOUNT ORDER BY ? ASC ? ASC ? ASC");
		
		preparedStatement.setInt(1,savingAccount.getBankAccount().getAccountNumber());
		preparedStatement.setString(2,savingAccount.getBankAccount().getAccountHolderName());
		preparedStatement.setDouble(3, savingAccount.getBankAccount().getAccountBalance());
		ResultSet resultSet = preparedStatement.executeQuery();
		while (resultSet.next()) {
			int accountNumber = resultSet.getInt(1);
			String accountHolderName = resultSet.getString("account_hn");
			double accountBalance = resultSet.getDouble(3);
			boolean salary = resultSet.getBoolean("salary");
			SavingsAccount savingsAccount = new SavingsAccount(accountNumber, accountHolderName, accountBalance,
					salary);
			sortAccounts.add(savingsAccount);
		}
		return sortAccounts;
		}







}
