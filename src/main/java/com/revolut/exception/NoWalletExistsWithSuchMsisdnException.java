package com.revolut.exception;

public class NoWalletExistsWithSuchMsisdnException extends RuntimeException {

	public NoWalletExistsWithSuchMsisdnException(String msisdn) {
		super(msisdn);
	}
}
