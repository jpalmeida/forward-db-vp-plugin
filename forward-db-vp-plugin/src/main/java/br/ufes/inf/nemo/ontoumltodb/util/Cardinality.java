package br.ufes.inf.nemo.ontoumltodb.util;


public enum Cardinality {

	C1("1"), 
	C0_1("0..1"),
	C1_N("1..*"), 
	C0_N("0..*"), 
	X("uninformed");

	private final String display;

	private Cardinality(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
