package br.ufes.inf.nemo.ontoumltodb.transformation.tracer;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;

public class TracedNode {

	private Node nodeMapped;
	private ArrayList<Filter> filters;
	
	public TracedNode(Node node) {
		filters = new ArrayList<Filter>();
		this.nodeMapped = node;
	}
	
	public TracedNode(Node nodeMapped, NodeProperty propertyFilter, Object valueFilter) {
		filters = new ArrayList<Filter>();
		this.nodeMapped = nodeMapped;
		addFilter(propertyFilter, valueFilter);
	}
	
	public void setNodeMapped(Node nodeMapped) {
		this.nodeMapped = nodeMapped;
	}
	
	public Node getNodeMapped() {
		return this.nodeMapped;
	}
	
	public ArrayList<Filter> getFilters(){
		ArrayList<Filter> result = new ArrayList<Filter>();
		for (Filter filter : filters) {
			result.add(filter);
		}
		return result;
	}
	
	public void updateNode(Node newNodeMapped) {
		this.nodeMapped = newNodeMapped;
	}
	
	private Filter getFilter(NodeProperty property, Object valueFiltred) {
		for(Filter filter : filters) {
			if(filter.getFilterProperty().getName().equals(property.getName()))
				if(filter.getValue().toString().equals(valueFiltred.toString()))
				return filter;
		}
		return null;
	}
	
	public void addFilter(Filter filter) {
		if(getFilter(filter.getFilterProperty(), filter.getValue()) == null) {
			filters.add(filter);
		}
	}
	
	public void addFilter(NodeProperty propertyFilter, Object valueFilter) {
		if(propertyFilter != null && valueFilter != null) {
			filters.add(new Filter(propertyFilter, valueFilter));
		}
	}
//	Process restructured from version V0.2.
//	public void addFilter(NodeProperty propertyFilter, Object valueFilter, ArrayList<NodeProperty> mandatoryProperties) {
//		Filter newFilter;
//		if(propertyFilter != null && valueFilter != null) {
//			newFilter = new Filter(propertyFilter, valueFilter);
//			for(NodeProperty property : mandatoryProperties) {
//				newFilter.addMandatoryProperty(property);
//			}
//			filters.add(newFilter);
//		}
//	}
	
	public void removeFilterOfProperty(NodeProperty property) {
		
		for(int i = 0; i < filters.size(); i++) {
			if(filters.get(i).getFilterProperty().isMyId(property.getID())) {
				filters.remove(i);
				return;
			}
		}
	}
	
	public boolean isTracedNodeByName(Node node) {
		if (nodeMapped.getName().equals(node.getName()))
			return true;
		else return false;
	}
	
	public boolean isTracedNodeById(Node node) {
		if (nodeMapped.isMyId(node.getID()))
			return true;
		else return false;
	}
	
	
	public boolean hasFilter() {
		if(filters.size() > 0)
			return true;
		else return false;
	}
//	Process restructured from version V0.2.
//	public boolean hasMandatoryProperties() {
//		for(Filter filter : this.filters) {
//			if(filter.hasMandatoryProperties()) {
//				return true;
//			}
//		}
//		return false;
//	}
	
	public void updateTraceNode(Node from, Node to, NodeProperty discriminatorProperty, Object discriminatorValue) {
		nodeMapped = to;
		Filter newFilter;
		NodeProperty migratedProperty = getMigratedProperty(to, discriminatorProperty.getName());
		
		//update the filters cloned properties
		for(Filter filter : filters) {
			filter.setFilterProperty(getMigratedProperty(to, filter.getFilterProperty().getName()));
//			filter.updateMandatoryPrperties(to); Process restructured from version V0.2.
		}
		
		newFilter = new Filter(migratedProperty, discriminatorValue);
		
//		Process restructured from version V0.2.
//		for(NodeProperty fromProperty : from.getProperties()) {
//			if( ( !fromProperty.isNullable() ) && ( !fromProperty.isIdentifyOtherClass() ) ) {
//				newFilter.addMandatoryProperty(getMigratedProperty(to, fromProperty.getName()));
//			}
//		}
		filters.add(newFilter);
	}
	
	/*
	 * The process migrates by cloning. This function retrieves the cloned property.
	 */
	private NodeProperty getMigratedProperty(Node to, String propertyName) {
		for(NodeProperty nodeProperty : to.getProperties()) {
			if(nodeProperty.getName().equals(propertyName)  )
				return nodeProperty;
		}
		return null;
	}
	
	public ArrayList<NodeProperty> getMandatoryProperteOf(NodeProperty discriminatorProperty){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
		for(Filter filter : filters) {
//			mandatoryProperties = filter.getMandatoryProperteOf(discriminatorProperty);Process restructured from version V0.2.
			for(NodeProperty property : mandatoryProperties) {
				result.add(property);
			}
		}
		return result;
	}
	
	public boolean existsOriginalPropertyInNodeMapped(NodeProperty property) {
		for(NodeProperty nodeProperty : this.nodeMapped.getProperties()) {
			if(property.getName().equals(nodeProperty.getOriginalName()))
				return true;
		}
		return false;
	}
	
	public String toString() {
		String msg = "";
		boolean first = true;
		
		for (Filter filter : filters) {
			if(first) {
				msg += filter.toString();
				first = false;
			}
			else {
				msg += " | " + filter.toString();
			}
		}
		if(msg.length() > 1) {
			msg = " [" + msg + "]";
		}
		
		msg = nodeMapped.getName() + msg;
		
		return msg;
	}
}
