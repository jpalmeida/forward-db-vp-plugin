package br.ufes.inf.nemo.ontoumltodb.transformation.database;


public class SqlUtil {

	// mover este código para database.Generic
	public static String getStringValue(Object value) {
		String text = value.toString();
		text = text.toString().toUpperCase();

		if (value instanceof String) {
			return "'" + text + "'";
		} else {
			return text;
		}
	}
}
