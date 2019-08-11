package com.revolut.db;

import com.revolut.entity.Wallet;
import com.revolut.exception.NoWalletExistsWithSuchMsisdnException;

import java.util.Hashtable;

public class WalletDao {

	private static Hashtable<String, Wallet> walletsTable = new Hashtable<>();

	public WalletDao() {
		addRow(new Wallet("123000000001", "135790246800001", "Marcin Szymaniuk", "12125650001"));
		addRow(new Wallet("123000000002", "135790246800002", "Jeremy Sevellec", "12125650002"));
	}

	public void addRow(Wallet row) {
		walletsTable.put(row.getMsisdn(), row);
	}

	public Wallet getWalletByMsisdn(String msisdn) {
		Wallet wallet = walletsTable.get(msisdn);
		if (wallet == null)
			throw new NoWalletExistsWithSuchMsisdnException(msisdn);
		return wallet;
	}
}
