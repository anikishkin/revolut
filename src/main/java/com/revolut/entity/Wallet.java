package com.revolut.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Wallet {

	private String id;
	private String acctNum;
	private String holderName;
	private String msisdn;
}
