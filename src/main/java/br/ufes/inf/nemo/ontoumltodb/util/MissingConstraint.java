package br.ufes.inf.nemo.ontoumltodb.util;

public enum MissingConstraint {

	MC1_2("Missing Constraint 1 and 2"),
	MC1_2_Inverse("Inverse Missing Constraint 1 and 2"),
	MC3_4("Missing Constraint 3 and 4"),
	MC6("Missing Constraint 6");

	private final String display;

	private MissingConstraint(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
