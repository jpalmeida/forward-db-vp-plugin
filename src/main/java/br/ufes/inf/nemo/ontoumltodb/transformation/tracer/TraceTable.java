package br.ufes.inf.nemo.ontoumltodb.transformation.tracer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;

public class TraceTable {

	private Graph originalGraph;
	private ArrayList<TraceSet> traceSets;
	
	public TraceTable(Graph graph) {
		this.originalGraph = graph.clone();
		this.traceSets = new ArrayList<TraceSet>();
		mountcorrespondences(graph);
	}

	public Graph getOriginalGraph() {
		return this.originalGraph;
	}
	
	public ArrayList<TraceSet> getTraces() {
		ArrayList<TraceSet> result = new ArrayList<TraceSet>();
		for (TraceSet traceSet : traceSets) {
			result.add(traceSet);
		}

		Collections.sort(result, new Comparator<TraceSet>() {
			public int compare(TraceSet traceSet1, TraceSet traceSet2) {
				return traceSet1.getSourceNode().getName().compareTo(traceSet2.getSourceNode().getName());
			}
		});
		return result;
	}
	
	public TraceSet getTraceSetById(Node node) {
		for(TraceSet traceSet : this.traceSets) {
			if(traceSet.getSourceNode().isMyId(node.getID()))
				return traceSet;
		}
		return null;
	}
	
	public ArrayList<Trace> getTracesById(Node node) {
		for(TraceSet traceSet : this.traceSets) {
			if(traceSet.getSourceNode().isMyId(node.getID()))
				return traceSet.getTraces();
		}
		return null;
	}

	private void mountcorrespondences(Graph graph) {
		TraceSet newTraceSet;
		for (Node node : graph.getNodes()) {
			newTraceSet = new TraceSet(
					originalGraph.getNodeById(node.getID()), // original node
					graph.getNodeById(node.getID())); // node to be transformed/traced
			traceSets.add(newTraceSet);
		}
	}
	
	public void removeTracedNode(Node from) {
		for (TraceSet traceSet : this.traceSets) {
			if(traceSet.existsNode(from)) {
				traceSet.removeTracedNode(from);
			}
		}
	}
	
	public void addTargetNode(Node sourceNode, Node targetNode) {
		TraceSet traceSet = getTraceSetById(sourceNode);
		if(traceSet != null)
			traceSet.addTrace(targetNode);		
	}
	
	public void addTargetNode(Node from, Node to, NodeProperty discriminatorProperty, Object discriminatorValue) {
		for (TraceSet traceSet : this.traceSets) {
			if(traceSet.existsNode(from)) {
				traceSet.addTrace(from, to, discriminatorProperty, discriminatorValue);
			}
		}
	}
	
	public void updateTrace(Node from, Node to, NodeProperty discriminatorProperty, Object discriminatorValue) {
		for (TraceSet traceSet : this.traceSets) {
			if(traceSet.existsNode(from)) {
				traceSet.updateTrace(from, to, discriminatorProperty, discriminatorValue);
			}
		}
	}
	
	public void moveFilters(Node node) {
		for (TraceSet traceSet : this.traceSets) {
			if(traceSet.existsNode(node)) {
				traceSet.moveFilters(node);
			}
		}
	}
	
	/*
	 * Adds a new node originated from a multivalued property.
	 */
	public void addNodeGeneratedFromMultivaluedProperty(Node targetNode, Node newNode, NodeProperty propertyAffected) {
		for (TraceSet traceSet : this.traceSets) {
			if(traceSet.existsNode(targetNode)) {
				traceSet.addNodeGeneratedFromMultivaluedProperty(targetNode, newNode, propertyAffected);
			}
		}
	}
	
	public void addNodeGeneratedFromMultivaluedProperty(Node sourceNode, Node targetNode, Node newNode, NodeProperty propertyAffected) {
		TraceSet traceSet = getTraceSetById(sourceNode);
		if(traceSet != null)
			traceSet.addNodeGeneratedFromMultivaluedProperty(targetNode, newNode, propertyAffected);
	}
	
	
	/*
	 * Adds a new node between two nodes. Is is used solves N:N cardinality.
	 */
//	public void addIntermediateNode(Node node1, Node node2, Node newNode) {
//		for (TraceSet traceSet : this.traceSets) {
//			traceSet.addIntermediateNode(node1, node2, newNode);
//		}
//	}
	
	/*
	 * The new node must be added in the correct relative position for the 
	 * join to be done correctly.
	 */
	public void addIntermediateNode(Node newNode, GraphAssociation association){
		Node originalNode;
		GraphAssociation originalAssociation;
		
		originalAssociation = this.originalGraph.getAssociationByID(association.getOriginalAssociation().getID()); //complete original association; with original nodes
		
		originalNode = originalAssociation.getSourceNode();
		addIntermediateNodeInTrace(originalNode, newNode, association.getSourceNode());
		
		originalNode = originalAssociation.getTargetNode();
		addIntermediateNodeInTrace(originalNode, newNode, association.getTargetNode());
		
	}
	
	private void addIntermediateNodeInTrace(Node sourceNode, Node newNode, Node afterNode) {
		
		for(TraceSet traceSet : this.traceSets) {
			if(traceSet.getSourceNode().isMyId(sourceNode.getID())) {
				traceSet.addIntermediateNode(newNode, afterNode);
			}
		}
		
		for(Node specialization : sourceNode.getSpecializationNodes()) {
			addIntermediateNodeInTrace(specialization, newNode, afterNode);
		}
	}
	
	public void changeFiltersFromTo(Node from, Node to, boolean disassociateNodeFrom) {
		for (TraceSet traceSet : this.traceSets) {
			traceSet.changeFiltersFromTo(from, to, disassociateNodeFrom);
		}
	}
	
	public ArrayList<NodeProperty> getMandatoryProperteOf(NodeProperty discriminatorProperty){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
		
		for (TraceSet traceSet : this.traceSets) {
			mandatoryProperties = traceSet.getMandatoryProperteOf(discriminatorProperty);
			//Eliminate duplicated properties.
			for(NodeProperty property : mandatoryProperties) {
				if(!existsProperty(result, property)) {
					result.add(property);
				}
			}
		}		
		return result;
	}
	
	public boolean isSourceNodeMapped(Node node) {
		for (TraceSet traceSet : traceSets) {
			if(traceSet.isSourceNodeMapped(node))
				return true;
		}
		return false;				
	}
	
	public boolean existsProperty(ArrayList<NodeProperty> properties, NodeProperty evaluatedProperty) {
		for(NodeProperty property : properties) {
			if(property.isMyId(evaluatedProperty.getID()))
				return true;
		}
		return false;
	}
	
	public String getTraceSetSring(String className) {
		StringBuilder text = new StringBuilder();
		
		for (TraceSet traceSet : this.traceSets) {
			if(traceSet.getSourceNode().getName().equals(className))
				text.append(traceSet.toString());
		}
		return text.toString();
	}

	public String toString() {
		String msg =  ""; // "** TRACE  TABLE **";
		
		ArrayList<TraceSet> result = new ArrayList<TraceSet>();
		for (TraceSet traceSet : this.traceSets) {
			result.add(traceSet);
		}

		Collections.sort(result, new Comparator<TraceSet>() {
			public int compare(TraceSet traceSet1, TraceSet traceSet2) {
				return traceSet1.getSourceNode().getName().compareTo(traceSet2.getSourceNode().getName());
			}
		});
		
		for (TraceSet traceSet : result) {
			msg += "\n" + traceSet.toString() + ";";
		}
		return msg;
	}

}
