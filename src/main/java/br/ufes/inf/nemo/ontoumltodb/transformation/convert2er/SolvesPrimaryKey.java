package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;

public class SolvesPrimaryKey {

	public static void solves(Graph graph) {
		String pkName;
		NodeProperty property;
		String newID;

		for (Node node : graph.getNodes()) {
			pkName = node.getName() + "_id";
			
			newID = pkName +Increment.getNextS();

			property = new NodeProperty(node, newID, pkName, "int", false, false);

			property.setPrimaryKey(true);

			node.addPropertyAt(0, property);
		}
	}
}
