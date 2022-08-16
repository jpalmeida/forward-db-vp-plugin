package br.ufes.inf.nemo.ontoumltodb.util;

public enum Origin {

	CREATION("Creation"),
	FLATTENING("Flattening"),
	LIFTING("Lifting"),
	N2NASSOCIATION("N2NAssociation"),
	MULTIVALUEATTRIBUTE("MultivalueAttribute");

	private final String display;

	private Origin(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
