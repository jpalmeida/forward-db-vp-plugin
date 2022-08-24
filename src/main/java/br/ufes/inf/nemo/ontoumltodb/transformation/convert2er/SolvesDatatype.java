package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class SolvesDatatype {

	public static void solves(Graph graph, TraceTable traceTable) {
		
		for(Node datatypeNode : getDatatypes(graph)) {
			for(Node currentNode : graph.getNodes()) {
				evaluateNode(graph, traceTable, currentNode, datatypeNode);
			}
		}
		
		for(Node node : graph.getNodes()) {
			if(node.getStereotype() == Stereotype.DATATYPE) {
				if(node.getProperties().isEmpty() || node.getAssociations().isEmpty())
					graph.removeNode(node);
			}
		}
	}
	
	private static ArrayList<Node> getDatatypes(Graph graph){
		ArrayList<Node> datatypes = new ArrayList<Node>();
		
		for(Node node : graph.getNodes()) {
			if(node.getStereotype() == Stereotype.DATATYPE) {
				// If the class is datatype and has no attributes, then it must be 
				// treated as a primitive database type, like "Date" or "Datetime".
				if(!node.getProperties().isEmpty())
					datatypes.add(node);
			}
		}
		return datatypes;
	}
	
	public static void evaluateNode(Graph graph, TraceTable traceTable, Node currentNode, Node datatypeNode) {
		if(currentNode.getStereotype() == Stereotype.DATATYPE)
			return ;
		
		for(NodeProperty property : currentNode.getProperties()) {
			if(property.getDataType().equals(datatypeNode.getName()))
				solve(graph, traceTable, property, currentNode, datatypeNode);
		}
	}
	
	private static void solve(Graph graph, TraceTable traceTable, NodeProperty property, Node currentNode, Node datatypeNode) {
		currentNode.removeProperty(property.getID());
		
		for(NodeProperty nodeProperty : datatypeNode.getProperties()) {
			currentNode.addProperty(nodeProperty.clone(currentNode, Increment.getNextS()));
		}
		
//		if(property.isMultivalued())
//			generateAssociationWithDatatype(graph, traceTable, property , currentNode, datatypeNode);
//		else applyDatatypeAttributesInNode(currentNode, datatypeNode);
	}
	
//	private static void generateAssociationWithDatatype(Graph graph, TraceTable traceTable, NodeProperty property, Node currentNode, Node datatypeNode) {
//		GraphAssociation newAssociation = new GraphAssociation(
//				Increment.getNextS(), // id 
//				currentNode.getName() + "X" + datatypeNode.getName(), // name 
//				ElementType.ASSOCIATION, // elementType 
//				currentNode, // sourceNode 
//				Cardinality.C1, // sourceCardinality 
//				datatypeNode, // targetNode 
//				Cardinality.C0_N // targetCardinality
//				);
//		currentNode.addAssociation(newAssociation);
//		datatypeNode.addAssociation(newAssociation);
//		
//		traceTable.addNodeGeneratedFromMultivaluedProperty(currentNode, datatypeNode, property);
//		
//		graph.addAssociation(newAssociation);
//	}
	
//	private static void applyDatatypeAttributesInNode(Node currentNode, Node datatypeNode) {
//		for(NodeProperty property : datatypeNode.getProperties()) {
//			currentNode.addProperty(property.clone(currentNode, Increment.getNextS()));
//		}
//	}
}
