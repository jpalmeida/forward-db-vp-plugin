package br.ufes.inf.nemo.ontoumltodb.util;


public enum ElementType {

	PROPERTY("Property"),
	ENUMERATION("Enumeration"),
	CLASS("Class"),
	ASSOCIATION("Association"),
	GENERALIZATION("Generalization"), 
	GENERALIZATION_SET("GeneralizationSet");

	private final String display;

	private ElementType(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
