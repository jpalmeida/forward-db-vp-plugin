/**
 * Author: 
 */

package br.ufes.inf.nemo.ontoumltodb.util;

public enum DbmsSupported {
	GENERIC_SCHEMA("Generic Schema"), 
	MYSQL("MySql"), 
	H2("H2"), 
	SQLSERVER("SqlServer"), 
	ORACLE("Oracle"),
	POSTGRE("Postgre");

	private final String display;

	private DbmsSupported(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
