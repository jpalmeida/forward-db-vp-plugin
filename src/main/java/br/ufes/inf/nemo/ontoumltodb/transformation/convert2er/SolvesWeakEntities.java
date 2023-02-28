package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class SolvesWeakEntities {

	public static void run(Graph graph, TraceTable traceTable ) {
		
		for(Node node : graph.getNodes()) {
			if( Stereotype.isWeakStereotype(node.getStereotype()) ) {
				putAsProperty(node, graph);
				traceTable.removeTraceOfSourceNode(node);
				traceTable.removeNodeFromTraces(node);
			}
		}
	}
	
	private static void putAsProperty(Node node, Graph graph) {
		Node relatedNode;
		NodeProperty newProperty;
		String name;
		boolean acceptNull, isMultivalued;
		
		for(GraphAssociation association : node.getAssociations()) {
			relatedNode = association.getNodeEndOf(node);
			
			name = node.getName();
			name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
			
			if(		association.getCardinalityBeginOf(node) == Cardinality.C0_N ||
					association.getCardinalityBeginOf(node) == Cardinality.C1_N) 
				isMultivalued = true;
			else isMultivalued = false;
			
			if(association.getCardinalityEndOf(node) == Cardinality.C1)
				acceptNull = true;
			else acceptNull = false;
			
			
			if(node.getProperties().isEmpty()) {
				newProperty = new NodeProperty(
						relatedNode,
						Increment.getNextS(),
						name, 
						"String",
						acceptNull,
						isMultivalued
						);
				
				relatedNode.addPropertyAt(0, newProperty);
			}
			else {
				int index = 0;
				for(NodeProperty property : node.getProperties()) {
					newProperty = property.clone(relatedNode, Increment.getNextS());
					newProperty.setNullable(acceptNull);
					newProperty.setMultivalued(isMultivalued);
					relatedNode.addPropertyAt(index++, newProperty);
				}
			}
			graph.removeAssociation(association);
		}
		graph.removeNode(node);
	}
}
