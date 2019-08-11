package com.revolut.db;

import com.revolut.entity.Translog;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class TranslogDao {

	private static ConcurrentHashMap<String, Translog> translogTable = new ConcurrentHashMap<>();

	public void addRow(Translog row) {
		translogTable.put(row.getRefNum(), row);
	}

	public Collection<Translog> getAllRows() {
		return translogTable.values();
	}
}
