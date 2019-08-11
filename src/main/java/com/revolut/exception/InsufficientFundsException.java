package com.revolut.exception;

public class InsufficientFundsException extends RuntimeException {

	public InsufficientFundsException(long amount) {
		super(String.valueOf(amount));
	}
}
