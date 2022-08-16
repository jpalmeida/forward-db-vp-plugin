package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

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

public class SolvesCardinalityNtoN {

	public static void solves(Graph graph, TraceTable traceTable) {
		ArrayList<GraphAssociation> associations = getNtoNAssociations(graph);
		for (GraphAssociation association : associations) {
			resolveNtoN(association, graph, traceTable);
		}
	}

	/**
	 * Transform the cardinality N:N to 1:N and N:1 with a new node.
	 */
	public static void resolveNtoN(GraphAssociation association, Graph graph, TraceTable traceTable) {
		String nodeName = getNodeName(association, graph);
		Node newNode = new Node(
				Increment.getNextS(),
				nodeName,
				Stereotype.MIXIN,
				NodeOrigin.FROM_N_TO_N);
		
		newNode.setOrigin(Origin.N2NASSOCIATION);
		
		//SourceNode to newNode
		GraphAssociation newAssociation1 = new GraphAssociation(Increment.getNextS(), // ID
				"has_" + association.getSourceNode().getName(), // name
				association.getSourceNode(),// sourceNode
				Cardinality.C1, 			//sourceCardinality
				newNode, 					// targetNode
				Cardinality.C0_N); 			//targetCardinality
		newAssociation1.setOriginalAssociation(association.getOriginalAssociation());

		// TargetNode to newNode
		GraphAssociation newAssociation2 = new GraphAssociation(Increment.getNextS(), // ID
				"has_" + association.getTargetNode().getName(), // name
				association.getTargetNode(), // sourceNode
				Cardinality.C1, 			 // sourceCardinality
				newNode, 					 // targetNode
				Cardinality.C0_N); 			 // targetCardinality
		newAssociation2.setOriginalAssociation(association.getOriginalAssociation());
		
		newNode.addAssociation(newAssociation1);
		newNode.addAssociation(newAssociation2);
		association.getSourceNode().addAssociation(newAssociation2);
		association.getTargetNode().addAssociation(newAssociation2);

		graph.addNode(newNode);
		graph.addAssociation(newAssociation1);
		graph.addAssociation(newAssociation2);

		graph.removeAssociation(association);

		//Is not necessary to change the traceability if it does not involve an enumeration as 
		//it would not form a path for filters.
//		if(		association.getSourceNode().getStereotype() == Stereotype.ENUMERATION ||
//				association.getTargetNode().getStereotype() == Stereotype.ENUMERATION
//				) {
			//traceTable.addIntermediateNode(association.getSourceNode(), association.getTargetNode(), newNode);
		traceTable.addIntermediateNode(newNode, association);
//		}
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
	
	private static String getNodeName(GraphAssociation association, Graph graph) {
		String name;
		
		// To not create a intermediate table with duplicated name (e.g: PersonPerson). This appends in a self-association
		if(association.getSourceNode().getName().equals(association.getTargetNode().getName())){
			name = association.getOriginalAssociation().getSourceNode().getName() + association.getOriginalAssociation().getTargetNode().getName();
			if( graph.existsNodeName(name) ) {
				name = getGenericNodeName(association, graph);
			}
		}
		else {
			name = association.getSourceNode().getName() + association.getTargetNode().getName(); 
			if( graph.existsNodeName(name) ) {
				name = getGenericNodeName(association, graph);
			}
		}
		return name;
	}
	
	private static String getGenericNodeName(GraphAssociation association, Graph graph) {
		String name = association.getSourceNode().getName() + association.getOriginalAssociation().getTargetNode().getName();
		if( graph.existsNodeName(name) ) {
			name = association.getTargetNode().getName() + association.getOriginalAssociation().getSourceNode().getName();
			if( graph.existsNodeName(name) ) {
				name = association.getSourceNode().getName() + association.getTargetNode().getName() + association.getName();
				if( graph.existsNodeName(name) ) {
					name = association.getSourceNode().getName() + association.getOriginalAssociation().getTargetNode().getName() + association.getName();
					if( graph.existsNodeName(name) ) {
						name = association.getTargetNode().getName() + association.getOriginalAssociation().getSourceNode().getName() + association.getName();
						if( graph.existsNodeName(name) ) {
							name += Increment.getNextS();
						}
					}
				}
			}
		}
		return name;
	}
}
