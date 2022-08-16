package br.ufes.inf.nemo.ontoumltodb.util;


public enum NodeOrigin {
	
	UNDEFINED("undefined"),
	FROM_N_TO_N("fromNtoN"),
	FROM_MODEL("fromModel"),
	FROM_ATTRIBUTE("fromAttribute"),
	FROM_GENERALIZATION_SET("fromGeneralizationSet");

	private final String display;

	private NodeOrigin(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
