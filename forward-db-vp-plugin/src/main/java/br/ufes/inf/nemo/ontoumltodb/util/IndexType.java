package br.ufes.inf.nemo.ontoumltodb.util;

public enum IndexType {
	
	NOINDEX("noIndex"),
	INDEX("index"),
	//GROUPINDEXS("groupIndex"),
	//UNIQUEINDEX("uniqueIndex"),
	UNIQUEINDEXSWITHFK("UniqueIndexWithFk");

	private final String display;

	private IndexType(String s) {
		display = s;
	}

	@Override
	public String toString() {
		return display;
	}
}
