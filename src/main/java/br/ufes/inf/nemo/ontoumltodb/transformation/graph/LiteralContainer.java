package br.ufes.inf.nemo.ontoumltodb.transformation.graph;

import java.util.ArrayList;

public class LiteralContainer {

	private ArrayList<String> literals;
	public LiteralContainer() {
		this.literals = new ArrayList<String>();
	}
	public void addLiteral(String literal) {
		this.literals.add(literal);
	}
	
	public ArrayList<String> getLiterals(){
		ArrayList<String> result = new ArrayList<String>();
		
		for(String literal : this.literals) {
			result.add(literal);
		}
		return result;
	}
	
	public String toString() {
		if(this.literals.size() == 0)
			return "";
		
		String text = "\n\tENUMERATION [";
		
		for (String literal : this.literals) {
			 text += literal + " | ";
		 }
		 text += "]";
		
		return text;
	}
}
