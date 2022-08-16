package br.ufes.inf.nemo.ontoumltodb.transformation;

public class Statistic {

	private static int qtdMC12 = 0;
	private static int qtdMC34 = 0;
	private static int qtdMC6 = 0;
	
	
	public static int getQtd() {
		return qtdMC12 + qtdMC34 + qtdMC6;
	}
	
	public static int getQtdMC12() {
		return qtdMC12;
	}
	
	public static int getQtdMC34() {
		return qtdMC34;
	}
	
	public static int getQtdMC6() {
		return qtdMC6;
	}
	
	public static void addMC12() {
		qtdMC12++;
	}
	
	public static void addMC34() {
		qtdMC34++;
	}
	
	public static void addMC6() {
		qtdMC6++;
	}
	
	public static void initializes() {
		qtdMC12 = 0;
		qtdMC34 = 0;
		qtdMC6 = 0;
	}
}
