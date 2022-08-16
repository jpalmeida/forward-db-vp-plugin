package br.ufes.inf.nemo.ontoumltodb.transformation.obda;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;

public class GenerateObdaMappingId {

	public static String generate(String project, Node sourceNode, boolean first) {
		String text = "";

		text += "mappingId    ";
		text += project;
		text += "-";
		text += sourceNode.getName();
		// when a class is mapped to multiple classes.
		if (!first) {
			text += Increment.getNext();
		}
		text += "\n";

		return text;
	}
}
