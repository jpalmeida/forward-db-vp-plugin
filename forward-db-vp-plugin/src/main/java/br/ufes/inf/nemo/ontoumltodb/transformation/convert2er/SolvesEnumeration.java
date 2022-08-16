package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
//import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
//import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class SolvesEnumeration {

	public static void solves(Graph graph, TraceTable traceTable, boolean enumToLookupTable) {
		if (enumToLookupTable) {
			//transformEnumToLookupTables(graph, traceTable);
		} else {
			applyEnumToFilds(graph, traceTable);
		}
	}

	// **********************************************************
	// *** Methodos to apply enumerations to fields
	// **********************************************************
	private static void applyEnumToFilds(Graph graph, TraceTable traceTable) {
		ArrayList<Node> nodesToDestroy = new ArrayList<Node>();
		ArrayList<GraphAssociation> associationsToRemove = new ArrayList<GraphAssociation>();

		for (Node node : graph.getNodes()) {
			if (node.getStereotype() == Stereotype.ENUMERATION) {
				associationsToRemove.clear();

				for (GraphAssociation association : node.getAssociations()) {
					// Transforms the enumeration into a column
					addEnumerationColumn(node, association, traceTable);
					nodesToDestroy.add(node);
					associationsToRemove.add(association);
					
					traceTable.changeFiltersFromTo(node, association.getNodeEndOf(node), true);
				}
				graph.removeAssociations(associationsToRemove);
			}
		}
		graph.removeNodes(nodesToDestroy);
	}
	
	private static void addEnumerationColumn(Node enumNode, GraphAssociation association, TraceTable traceTable) {
		Node targetNode;
		Cardinality cardinalityOfEnum;
		boolean isNull;
		boolean isMultivalued;

		targetNode = getTargetNode(enumNode, association);
		cardinalityOfEnum = getCardinalityOf(enumNode, association);

		if (cardinalityOfEnum == Cardinality.C0_1 || cardinalityOfEnum == Cardinality.C1)
			isMultivalued = false;
		else
			isMultivalued = true;

		if (cardinalityOfEnum == Cardinality.C0_1 || cardinalityOfEnum == Cardinality.C0_N)
			isNull = true; // accept null
		else
			isNull = false; // not accept null

		for (NodeProperty property : enumNode.getProperties()) {
			property.setNullable(isNull);
			property.setMultivalued(isMultivalued);
			targetNode.addProperty(property);
		}
	}

	private static Node getTargetNode(Node node, GraphAssociation association) {
		if (association.getSourceNode() == node)
			return association.getTargetNode();
		else
			return association.getSourceNode();
	}

	private static Cardinality getCardinalityOf(Node node, GraphAssociation association) {
		if (association.getSourceNode() == node)
			return association.getSourceCardinality();
		else
			return association.getTargetCardinality();
	}

	// **********************************************************
	// *** Methodos to transform enumerations to lookup tables
	// **********************************************************
	/*
	// SEE SolvesMultivaluedProperty
	private static void transformEnumToLookupTables(Graph graph, TraceTable traceTable) {

		//NodePropertyEnumeration enumeration;
		ArrayList<NodeProperty> propertiesToRemove;
		ArrayList<Node> nodesToAdd = new ArrayList<Node>();
		ArrayList<GraphAssociation> associationsToAdd = new ArrayList<GraphAssociation>();
		Node newNode;
		GraphAssociation newAssociation;
		NodeProperty propertyToRemove;
		Cardinality cardinalityEnd, cardinalityBegin;
		for (Node node : graph.getNodes()) { // there is a insert into "nodes" into loop
			
			propertiesToRemove = new ArrayList<NodeProperty>();
			
			for(NodeProperty property : node.getProperties()) {
				if(property instanceof NodePropertyEnumeration) {
					newNode = new Node(Increment.getNextS(), property.getName(), null);
					newNode.addProperty(property);
					
					if(property.isNullable()) {
						if(property.isMultivalued()) {
							cardinalityBegin = Cardinality.C0_N;
							cardinalityEnd = Cardinality.C0_N;
						}
						else {
							cardinalityBegin = Cardinality.C1_N;
							cardinalityEnd = Cardinality.C1_N;
						}
					}
					else {
						
					}
					
					newAssociation = new GraphAssociation(
							Increment.getNextS(), 
							"has" + property.getName(), 
							node, 
							Cardinality.C1, 
							newNode,
							Cardinality.C1_N);
					
					nodesToAdd.add(newNode);
					associationsToAdd.add(newAssociation);
					
					propertiesToRemove.add(property);
				}
			}
			while(propertiesToRemove.size() > 0) {
				propertyToRemove = propertiesToRemove.remove(0);
				node.removeProperty(propertyToRemove.getID());
			}
		}
		while(nodesToAdd.size() > 0) {
			newNode = nodesToAdd.remove(0);
			graph.addNode(newNode);
		}
		
		while(associationsToAdd.size() > 0) {
			newAssociation = associationsToAdd.remove(0);
			graph.addAssociation(newAssociation);
		}
	}
	*/
}
