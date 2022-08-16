package br.ufes.inf.nemo.ontoumltodb.util;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;

public class Util {
	private static int maxSize = 50;
	private static int tabSpaces = 4;
	
	public static int getMaxSizeNames() {
		return maxSize;
	}
	
	public static int getTabSpaces() {
		return tabSpaces;
	}
	
	public static String getSpaces(String name, int qtd){
		
		int tam = name.length();
	    StringBuilder spaces = new StringBuilder();
	    
	    spaces.append(" ");

	    tam++;

	    while (tam < qtd) {
	      spaces.append(" ");
	      tam++;
	    }
	    return spaces.toString();
		
//	    int tam = name.length();
//	    String spaces = " ";
//
//	    tam++;
//
//	    while (tam < qtd) {
//	      spaces += ' ';
//	      tam++;
//	    }
//	    return spaces;
	  }

	public static String getStringValue(Object value) {
		String text = value.toString();
		text = text.toString().toUpperCase();

		if (value instanceof String) {
			return "'" + text + "'";
		} else {
			return text;
		}
	}
	
	public static String removeSpecialChar(String name) {
		StringBuilder newName = new StringBuilder();
		Character evaluatedChar;
		
		for(int i = 0; i < name.length(); i++) {
			evaluatedChar = name.charAt(i);
			if(! isSpecialChar( evaluatedChar)) {
				newName.append(evaluatedChar);
			}
		}
		return newName.toString();
	}
	
	private static boolean isSpecialChar(char myChar) {
		if( 	(myChar < 48 || myChar > 57) &&
				(myChar < 65 || myChar > 90) && 
				(myChar < 97 || myChar > 122) &&
				(myChar != 95) ) {
			return true;
		}
		return false;
	}
	
	public static boolean existsAssociationInNewNode(Node newNode, Node targetNode, GraphAssociation newAssociation) {
		if(newAssociation.getOriginalAssociation() == null)
			return false;

		for(GraphAssociation association : newNode.getAssociations()) {
			if(association.getOriginalAssociation() != null) {
				// If ID is the same
				if(newAssociation.getOriginalAssociation().isMyId(association.getOriginalAssociation().getID())) {
					// To not contemplate self-association
					if(newAssociation.getSourceNode().isMyId(newAssociation.getTargetNode().getID())) {
						return false;
					}
					else {
						if(	(	association.getSourceNode().isMyId(newNode.getID()) && 
								association.getTargetNode().isMyId(targetNode.getID())  
							) ||
							(
								association.getTargetNode().isMyId(newNode.getID()) && 
								association.getSourceNode().isMyId(targetNode.getID())
							)
						){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static String padronizeNameSize(String name) {		
		String newName = "";
		
		if(name.length() <= maxSize)
			return name;
		
		if(name.substring(name.length() - 3).equals("_id")) {
			newName = name.substring(0, maxSize - 3) + "_id";
		}
		else {
			newName = name.substring(0, maxSize) ;
		}
		return newName;
	}
	
	public static String padronizeNameSize(String name, String id) {
		if(name.length() <= maxSize)
			return name;
		
		String newName = "";
		int reduce = 3 + id.length(); // "_id" = 3 (length)
		
		if(name.length() <= maxSize)
			return name;
		
		newName = name.substring(0, maxSize - reduce) + id + "_id";
		
		return newName;
	}
}
