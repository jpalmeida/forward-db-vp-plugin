package br.ufes.inf.nemo.ontoumltodb.transformation.convert2er;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Increment;
import br.ufes.inf.nemo.ontoumltodb.util.IndexType;
import br.ufes.inf.nemo.ontoumltodb.util.NodeOrigin;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;

public class SolvesMultivaluedProperty {

	/*
	
	*/
	public static void run(Graph graph, TraceTable traceTable){
	    ArrayList<String> idToRemove = new ArrayList<String>();

	    for (Node node : graph.getNodes()) {
	    	idToRemove.clear();
		      
		    for (NodeProperty property : node.getProperties()) {
		    	if (property.isMultivalued()) {
		    		transformPropertyIntoNode(property, node, graph, traceTable);
			        idToRemove.add(property.getID());
		    	}
		    }
		    while (!idToRemove.isEmpty()) {
		        node.removeProperty(idToRemove.remove(0));
		    }
		}
	}

	 private static void transformPropertyIntoNode(NodeProperty property, Node node, Graph graph, TraceTable traceTable) {
	    Node newNode;
	    GraphAssociation association;
	    String nodeName = property.getName();
	    String subStringName;
	    
	    if(nodeName.length() > 4) {
		    subStringName = nodeName.substring(nodeName.length() - 4, nodeName.length()); 
		    
		    if( subStringName.equalsIgnoreCase("enum")) {
		    	nodeName = nodeName.substring(0, nodeName.length() - 4);
		    }
	    }
	    
	    if(graph.getNodeByName(nodeName) != null) {
	    	nodeName = nodeName + "Enumeration";
	    }
	    
	    if(!graph.existsNodeName(nodeName)) {
	    	newNode = new Node(
		    		Increment.getNextS(), 
		    		nodeName, 
		    		null, //Stereotype.MIXIN, 
		    		NodeOrigin.FROM_ATTRIBUTE
		    	);
	    	newNode.setOrigin(Origin.MULTIVALUEATTRIBUTE);
	    	graph.addNode(newNode);
	    }
	    else {
	    	newNode = graph.getNodeByName(nodeName);
	    }
	    
	    property.setNullable(false);
	    property.setMultivalued(false);
	    
	    if(property.isGeneratedFromTransformationProcess())
	    	property.setIndexType(IndexType.UNIQUEINDEXSWITHFK);
	    else property.setIndexType(IndexType.INDEX);
	    
	    newNode.addProperty(property);
	    
	    association = new GraphAssociation(
	    		Integer.toString(Increment.getNext()),
	    		"has" + property.getName() + "_" + Increment.getNextS(),
	    		node,
	    		Cardinality.C1,
	    		newNode,
	    		Cardinality.C1_N
			);

	    newNode.addAssociation(association);
	    node.addAssociation(association);
	    
	    graph.addAssociation(association);
	    
	    //For tracing
	    traceTable.addNodeGeneratedFromMultivaluedProperty(node, newNode, property);
//	    if(property instanceof NodePropertyEnumeration) {
//	    	NodePropertyEnumeration enumProperty = (NodePropertyEnumeration) property;
//	    	
//	    	if(enumProperty.getOriginGeneralizationSet() != null) {
//	    		
//	    		traceTable.addNodeGeneratedFromMultivaluedProperty(enumProperty.getOriginGeneralizationSet().getGeneral(), node, newNode, property);
//	    		
//	    		for(GraphGeneralization generalization : enumProperty.getOriginGeneralizationSet().getGeneralizations()) {
//	    			traceTable.addNodeGeneratedFromMultivaluedProperty(generalization.getSpecific() ,node, newNode, property);
//	    		}
//	    	}
//	    	else {
//	    		traceTable.addNodeGeneratedFromMultivaluedProperty(node, newNode, property);
//	    	}
//	    }else {
//	    	traceTable.addNodeGeneratedFromMultivaluedProperty(node, newNode, property);
//	    }
	    
	  }
	 
	 
//	 /*
//	  * To validate the filling of attributes (mandatarty) according to the value of the 
//	  * multivalued attribute, the attributes dependent on the multivalued attribute 
//	  * must also be moved to the multivalued attribute table, otherwise it is impossible 
//	  * to validate their filling.
//	  */
//	 private static void changeMandataryAttribures(Node sourceNode, Node targetNode, NodeProperty propertyChanged, TraceTable traceTable) {
//		ArrayList<NodeProperty> mandatoryProperties = traceTable.getMandatoryProperteOf(propertyChanged);
//		for(NodeProperty property : mandatoryProperties) {
//			sourceNode.removeProperty(property.getID());
//			targetNode.addProperty(property);
//		}
//		 
//	 }
}
