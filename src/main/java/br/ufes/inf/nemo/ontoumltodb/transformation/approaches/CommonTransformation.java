package br.ufes.inf.nemo.ontoumltodb.transformation.approaches;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.ElementType;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.NodeOrigin;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class CommonTransformation {

	protected boolean isTransformaNtoNFirst = false;
	
	protected void resolveNtoN(Graph graph, TraceTable traceTable) {
		ArrayList<GraphAssociation> associations = getNtoNAssociations(graph);
		for (GraphAssociation association : associations) {
			resolveNtoN(association, graph, traceTable);
		}
	}
	
	private static ArrayList<GraphAssociation> getNtoNAssociations(Graph graph) {
		ArrayList<GraphAssociation> associations = graph.getAssociations(ElementType.ASSOCIATION);
		ArrayList<GraphAssociation> result = new ArrayList<GraphAssociation>();

		for (GraphAssociation association : associations) {
			if (	(association.getSourceCardinality() == Cardinality.C0_N || association.getSourceCardinality() == Cardinality.C1_N)
					&& 
					(association.getTargetCardinality() == Cardinality.C0_N || association.getTargetCardinality() == Cardinality.C1_N)
			) {
				result.add(association);
			}
		}
		return result;
	}
	
	/**
	 * Transform the cardinality N:N to 1:N and N:1 with a new node.
	 */
	private static void resolveNtoN(GraphAssociation association, Graph graph, TraceTable traceTable) {
		String nodeName = getNodeName(association, graph);
		Node newNode = new Node(
				Increment.getNextS(),
				nodeName,
				Stereotype.RELATOR,
				NodeOrigin.FROM_N_TO_N);
		
		newNode.setOrigin(Origin.N2NASSOCIATION);
		
		//SourceNode to newNode
		GraphAssociation newAssociation1 = new GraphAssociation(Increment.getNextS(), // ID
				"has" + association.getSourceNode().getName(), // name
				association.getSourceNode(),// sourceNode
				Cardinality.C1, 			//sourceCardinality
				newNode, 					// targetNode
				Cardinality.C0_N); 			//targetCardinality
		newAssociation1.setOriginalAssociation(association.getOriginalAssociation());

		// TargetNode to newNode
		GraphAssociation newAssociation2 = new GraphAssociation(Increment.getNextS(), // ID
				"has" + association.getTargetNode().getName(), // name
				association.getTargetNode(), // sourceNode
				Cardinality.C1, 			 // sourceCardinality
				newNode, 					 // targetNode
				Cardinality.C0_N); 			 // targetCardinality
		newAssociation2.setOriginalAssociation(association.getOriginalAssociation());
		
		graph.addNode(newNode);
		graph.addAssociation(newAssociation1);
		graph.addAssociation(newAssociation2);

		graph.removeAssociation(association);

		traceTable.addIntermediateNodeOnlyDirectAssociatedClass(newNode, association);
	}
	
	private static String getNodeName(GraphAssociation association, Graph graph) {
		String name;
		
		name = association.getSourceNode().getName() + association.getTargetNode().getName();
		
		if( graph.existsNodeName(name) ) {
			name += Increment.getNextS();
		}
		
		return name;
	}
	
}
