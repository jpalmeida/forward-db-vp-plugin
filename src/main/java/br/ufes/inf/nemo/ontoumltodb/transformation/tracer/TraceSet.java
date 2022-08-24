package br.ufes.inf.nemo.ontoumltodb.transformation.tracer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;

public class TraceSet {

	private Node originalNode;
	private Map<String, Trace> traces;

	public TraceSet(Node sourceNode, Node node) {
		this.traces = new HashMap<>();
		this.originalNode = sourceNode;
		addTrace(node);
	}
	
	public Node getSourceNode() {
		return this.originalNode;
	}
	
	public ArrayList<Trace> getTraces(){
		ArrayList<Trace> result = new ArrayList<Trace>();
		for (Trace trace : traces.values()) {
			result.add(trace);
		}
		return result;
	}
	
	public void removeTracedNode(Node to) {
		String toRemove = null;
		Trace trace;
		for (String key : traces.keySet()) {
			trace = traces.get(key);
             if(trace.existsNode(to)){
            	 toRemove = key;
             }
		}
		traces.remove(toRemove);
	}
	
	public void addTrace(Node newNode) {
		this.traces.put(newNode.getID(), new Trace(newNode));
	}
	
	public void addTrace(Node sourceNode, Node targetNode, NodeProperty discriminatorProperty, Object discriminatorValue) {
		Trace newTrace = new Trace(targetNode, discriminatorProperty, discriminatorValue);	
		this.traces.put(targetNode.getID(), newTrace);
	}
	
	public void updateFilters(Node from, Node to) {
		ArrayList<Filter> filtersFrom = getFiltersOf(from);
		Trace trace = getTraceOf(to);
		trace.addFilters(filtersFrom, to);
	}
	
	public void updateTrace(Node from, Node to, NodeProperty discriminatorProperty, Object discriminatorValue) {
		for (Trace trace : this.traces.values()) {
			if(trace.existsNode(from)) {
				trace.updateTrace(from, to, discriminatorProperty, discriminatorValue);
			}
		}
	}
	
	public void moveFilters(Node node) {
		ArrayList<Filter> filters = getFiltersOf(node);
		for (Trace trace : this.traces.values()) {
			trace.addFiltersForAllTracedNodes(filters);
		}
	}
	
	private Trace getTraceOf(Node node) {
		for (Trace trace : this.traces.values()) {
			if(trace.existsNode(node)) {
				return trace;
			}
		}
		return null;
	}
	
	private ArrayList<Filter> getFiltersOf(Node node){
		for (Trace trace : this.traces.values()) {
			if(trace.existsNode(node)) {
				return trace.getFilters();
			}
		}
		return null;
	}
	
	public boolean hasFilter() {
		for (Trace trace : this.traces.values()) {
			if(trace.hasFilter())
				return true;
		}
		return false;
	}
	
	public boolean existsNode(Node node) {
		for(Trace trace : this.traces.values()) {
			if(trace.existsNode(node))
				return true;
		}
		return false;
	}
	
	public boolean isSourceNodeMapped(Node node) {
		if(traces.get(node.getID()) != null)
			return true;
		else return false;		
	}
	
	public void addNodeGeneratedFromMultivaluedProperty(Node targetNode, Node newNode, NodeProperty propertyAffected) {
		for(Trace trace : this.traces.values()) {
			if(trace.existsNode(targetNode)) {
				if(propertyAffected.getOrigin() == Origin.LIFTING) {
					for(Filter filter : trace.getFilters()) {
						if(filter.getFilterProperty().getName().equals(propertyAffected.getName())) {
							trace.addNodeGeneratedFromMultivaluedProperty(targetNode, newNode, propertyAffected);
						}
					}
				}
				else{
					trace.addNodeGeneratedFromMultivaluedProperty(targetNode, newNode, propertyAffected);
				}
			}
		}
	}
	
	public void addIntermediateNode(Node newNode, Node afterNode) {
		for (Trace trace : this.traces.values()) {
			trace.addIntermediateNode(newNode, afterNode);
		}
	}
	
	public void changeFiltersFromTo(Node from, Node to, boolean disassociateNodeFrom) {
		for (Trace trace : this.traces.values()) {
			trace.changeFiltersFromTo(from, to, disassociateNodeFrom);
		}
	}
	
//	Process restructured from version V0.2.
//	public ArrayList<NodeProperty> getMandatoryProperteOf(NodeProperty discriminatorProperty){
//		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
//		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
//		for (Trace trace : this.traces.values()) {
//			mandatoryProperties = trace.getMandatoryProperteOf(discriminatorProperty);
//			for(NodeProperty property : mandatoryProperties) {
//				result.add(property);
//			}
//		}
//		return result;
//	}

	public String toString() {
		String msg = "TRACE SET: " + this.originalNode.getName();;
		
		for (Trace trace : this.traces.values()) {
			msg +=  "\n\t" + trace.toString();
		}

		return msg;
	}

}
