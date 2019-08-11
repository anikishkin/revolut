package com.revolut.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Account {

	private String acctNum;
	private long balance;
	private String currency;

}
