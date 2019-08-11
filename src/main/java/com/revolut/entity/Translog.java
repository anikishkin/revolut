package com.revolut.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Translog {

	private String refNum;
	private String src;
	private String dst;
	private String transType;
	private long amount;
	private String currency;
}
