package br.ufes.inf.nemo.ontoumltodb.transformation.approaches;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.approaches.process.Flatting;
import br.ufes.inf.nemo.ontoumltodb.transformation.approaches.process.Lifting;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class OneTablePerKind implements IStrategy{

	public void run(Graph graph, TraceTable traceTable) {
		
	    runFlattening(graph, traceTable);
	    
	    runLifting(graph, traceTable);
	}
	
	//*************************************
	//*** F L A T T E N I N G
	//*************************************
	private void runFlattening(Graph graph, TraceTable traceTable) {
		Node node = getTopLevelNonSortal(graph);

		while (node != null) {
			Flatting.run(node, graph, traceTable);
			node = getTopLevelNonSortal(graph);
		}	
	}
	
	private Node getTopLevelNonSortal(Graph graph) {
		for (Node node : graph.getNodes()) {
			if (	Stereotype.isNonSortal(node.getStereotype()) && 
					!node.isSpecialization() && 
					node.hasSpecialization() // Allows the generation of a table with a "non-sortal" without heirs.
			) {
				return node;
			}
		}
		return null;
	}
	
	//*************************************
	//*** L I F T I N G
	//*************************************
	private void runLifting(Graph graph, TraceTable traceTable) {
		Node node = getLeafNode(graph);
	    
		while (node != null) {
			Lifting.run(node, graph, traceTable);
			node = getLeafNode(graph);
		}
	}
	
	private Node getLeafNode(Graph graph) {
		ArrayList<Node> roots = graph.getRootNodes();
		Node root, leafNode;
		
		if(roots.size() == 0)
			return null;
		
		root = roots.get(0);
		
		leafNode = getBottomNode(root);
		
		return leafNode;
	}
	
	private Node getBottomNode(Node node) {
		Node nextSubNode = null;
		ArrayList<Node> specializationNodes = node.getSpecializationNodes();
		
		//It is not necessary to retrieve the "deepest" node of the tree, but the deepest node of any branch.
		for(Node subNode : specializationNodes) {
			if(subNode.getSpecializationNodes().size() > 0)
				nextSubNode = subNode;
		}
		if(nextSubNode != null)
			return getBottomNode(nextSubNode);
		else return specializationNodes.get(0);
	}	
}
