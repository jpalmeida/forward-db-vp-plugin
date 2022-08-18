package br.ufes.inf.nemo.ontoumltodb.transformation.approaches.process;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralization;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphGeneralizationSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class Flatting {
	
	public static void run(Node flattenNode, Graph graph, TraceTable traceTable) {
		traceTable.removeTracedNode(flattenNode);
		
		for (GraphGeneralization generalization : flattenNode.getGeneralizations()) {
			flattenGeneralization(generalization);
			traceTable.addTargetNode(generalization.getGeneral(), generalization.getSpecific());
		}

		for (GraphAssociation association : flattenNode.getAssociations()) {
			flattenAssociation(flattenNode, association, graph, traceTable);
		}
		
		for (GraphGeneralizationSet gs : flattenNode.getGeneralizationSets()) {
			graph.removeGeneralizationSet(gs);
		}
		
		graph.removeNode(flattenNode);
	}

	private static void flattenGeneralization(GraphGeneralization generalization) {
		NodeProperty newProperty;
		ArrayList<NodeProperty> newProperties = new ArrayList<NodeProperty>();
		
		// The generalization is not removed from the node here because when removing the
		// node, its associations are also removed.
		ArrayList<NodeProperty> properties = generalization.getGeneral().getProperties();
		for (NodeProperty nodeProperty : properties) {
			newProperty = nodeProperty.clone(generalization.getGeneral(), Increment.getNextS());
			newProperty.setOwnerNode(generalization.getSpecific());
			if(newProperty.getRecivedBy() == Origin.CREATION) { // Change only one time
				newProperty.setRecivedBy(Origin.FLATTENING);
			}
			
			if(!generalization.getSpecific().existsPropertyName(newProperty.getName())) {
				newProperties.add(newProperty);
			}
		}
		generalization.getSpecific().addPropertiesAt(0, newProperties);
	}

	private static void flattenAssociation(Node flattenNode, GraphAssociation association, Graph graph, TraceTable traceTable) {
		Node relatedNode;
		
		for (GraphGeneralization generalization : flattenNode.getGeneralizations()) {
			if(! Util.existsAssociationInNewNode(flattenNode, generalization.getSpecific(), association)) {
				flattenAssociationWith(flattenNode, generalization.getSpecific(), association, graph, traceTable);
				
				// Missing Constraint 1. Inverse checking
				if(	(association.getCardinalityEndOf(flattenNode) == Cardinality.C1 || association.getCardinalityEndOf(flattenNode) == Cardinality.C0_1) &&
					(association.getCardinalityBeginOf(flattenNode) != Cardinality.C0_N && association.getCardinalityBeginOf(flattenNode) != Cardinality.C1_N)
				){
					generalization.getSpecific().addMissingConstraint(flattenNode, association, MissingConstraint.MC1_2_Inverse);
				}
			}
		}
		
		// Missing Constraint 1
		if(association.getCardinalityBeginOf(flattenNode) == Cardinality.C0_1 || association.getCardinalityBeginOf(flattenNode) == Cardinality.C1) {
			relatedNode = association.getNodeEndOf(flattenNode);
			relatedNode.addMissingConstraint(flattenNode, association, MissingConstraint.MC1_2);
		}
		
		graph.removeAssociation(association);
	}

	private static void flattenAssociationWith(Node flattenNode, Node toNode, GraphAssociation association, Graph graph, TraceTable traceTable) {
		GraphAssociation newAssociation = association.clone( Increment.getNextS(), null, null);
		newAssociation.setOriginalAssociation(traceTable.getOriginalGraph().getAssociationByID(association.getID()));

		newAssociation.setNodeNameRemoved(flattenNode.getName()); // this is important when there is a name collision in
																// the FK name propagation process.
		
		if (association.getSourceNode() == flattenNode) {
			newAssociation.setSourceNode(toNode);
			newAssociation.setSourceCardinality(getNewCardinality(association.getSourceCardinality()));
		}
			//} else {
		if (association.getTargetNode() == flattenNode) {
			newAssociation.setTargetNode(toNode);
			newAssociation.setTargetCardinality(getNewCardinality(association.getTargetCardinality()));
		}
		graph.addAssociation(newAssociation);
	}

	private static Cardinality getNewCardinality(Cardinality oldCardinality) {
		if (oldCardinality == Cardinality.C1) {
			return Cardinality.C0_1;
		} else if (oldCardinality == Cardinality.C1_N) {
			return Cardinality.C0_N;
		} else
			return oldCardinality;
	}

}
