package br.ufes.inf.nemo.ontoumltodb.util;

public class Increment {
	private static int next = 0;

	public static int getNext() {
		if (next == 0)
			next = 1;
		return next++;
	}
	
	public static String getNextS() {
		return Integer.toString(getNext());
	}
	
	public static void inicialzate() {
		next = 1;
	}
}
