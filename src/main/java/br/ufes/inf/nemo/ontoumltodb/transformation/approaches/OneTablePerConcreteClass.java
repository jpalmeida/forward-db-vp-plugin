package br.ufes.inf.nemo.ontoumltodb.transformation.approaches;

import br.ufes.inf.nemo.ontoumltodb.transformation.approaches.process.Flatting;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class OneTablePerConcreteClass implements IStrategy{

	public void run(Graph graph, TraceTable traceTable) {
		runFlattening(graph, traceTable);
	}
	
	private void runFlattening(Graph graph, TraceTable traceTable) {
		Node node = getAbstractNode(graph);

		while (node != null) {
			Flatting.run(node, graph, traceTable);
			node = getAbstractNode(graph);
		}	
	}
	
	public Node getAbstractNode(Graph graph) {
		for (Node node : graph.getNodes()) {
			if (	(
						//All non-sortals are abstracts.
						Stereotype.isNonSortal(node.getStereotype()) ||
						node.getStereotype() == Stereotype.ABSTRACT
					) && 
					!node.isSpecialization() && 
					node.hasSpecialization() // Allows the generation of a table with a "non-sortal" without heirs.
			) {
				return node;
			}
		}
		return null;
	}
}
