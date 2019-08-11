package com.revolut.db;

import com.revolut.entity.Account;
import com.revolut.exception.InsufficientFundsException;

import java.util.*;

public class AccountDao {

	private static Map<String, Account> accountsTable = Collections.synchronizedMap(new HashMap<>());

	public AccountDao() {
		addRow(new Account("135790246800001", 100000, "840"));
		addRow(new Account("135790246800002", 200000, "840"));
	}

	public void addRow(Account row) {
		accountsTable.put(row.getAcctNum(), row);
	}

	public synchronized void debitCredit(String srcAcctNum, String dstAcctNum, long amount) {
		debit(srcAcctNum, amount);
		credit(dstAcctNum, amount);
	}

	public void credit(String acctNum, long amount) {
		debit(acctNum, -amount);
	}

	public synchronized void debit(String acctNum, long amount) {
		Account acct = accountsTable.get(acctNum);
		long bal = acct.getBalance();
		if (bal < amount)
			throw new InsufficientFundsException(amount);
		acct.setBalance(bal - amount);
	}

	public Collection<Account> getAllRows() {
		return accountsTable.values();
	}
}
