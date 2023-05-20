package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class SolvesName {

	public static void solves(Graph graph, boolean isStandardizeNames) {
		if (isStandardizeNames)
			standardizeName(graph);
		
		checkNames(graph);
	}
	
	private static void standardizeName(Graph graph) {
		for (Node node : graph.getNodes()) {
			node.setName(adjust(node.getName()));
			for (NodeProperty property : node.getProperties()) {
				property.setName(adjust(property.getName()));
				if (property instanceof NodePropertyEnumeration) {
					adjustEnumerationValues((NodePropertyEnumeration) property);
				}
			}
		}
	}
	
	private static void adjustEnumerationValues(NodePropertyEnumeration enumeration) {
		ArrayList<String> values = enumeration.getValues();

		for (int index = 0; index < values.size(); index++) {
			values.set(index, values.get(index).toUpperCase());
		}
	}

	private static String adjust(String name) {
		String newName = "";
		int index = 0;

		// In order not to add "_" in the properties which are written in uppercase.
		while (index < name.length() && name.charAt(index) >= 'A' && name.charAt(index) <= 'Z') {
			newName += name.charAt(index);
			index++;
		}

		while (index < name.length()) {
			if (name.charAt(index) >= 'A' && name.charAt(index) <= 'Z') {
				newName += '_';
			}
			newName += name.charAt(index);
			index++;
		}
		return newName.toLowerCase();
	}
	
	private static void checkNames(Graph graph) {
		NodeProperty property1, property2;
		String newName;
		
		for (Node node : graph.getNodes()) {
			if(node.getName().length() > Util.getMaxSizeNames()) {
				node.setName(Util.padronizeNameSize(node.getName()));
			}
			for (NodeProperty property : node.getProperties()) {
				if(property.getName().length() > Util.getMaxSizeNames()) {
					property.setName(Util.padronizeNameSize(property.getName()));
				}
			}
			
			for(int i = 0; i < node.getProperties().size(); i++) {
				property1 = node.getProperties().get(i);
				
				for(int j = i+1; j < node.getProperties().size(); j++) {
					property2 = node.getProperties().get(j);
					
					if(property1.getName().equals(property2.getName())) {
						newName = property2.getName();
						
						newName = newName.substring(0, newName.length() - 3);
						newName += Increment.getNextS() + "_id";
						property2.setName(newName);
					}
				}
			}
		}
	}
}
