package br.ufes.inf.nemo.ontoumltodb.transformation.tracer;

import java.util.ArrayList;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;

public class Trace {

	private ArrayList<TracedNode> tracedNodes;

	Trace(Node node) {
		this.tracedNodes = new ArrayList<TracedNode>();
		this.tracedNodes.add( new TracedNode(node));
	}
	
	Trace(Node node,  NodeProperty discriminatorProperty, Object discriminatorValue) {
		this.tracedNodes = new ArrayList<TracedNode>();
		TracedNode tracedNode = new TracedNode(node);
		tracedNode.addFilter(discriminatorProperty, discriminatorValue);
		this.tracedNodes.add(tracedNode);
	}
	
	
	public ArrayList<TracedNode> getTracedNodes(){
		ArrayList<TracedNode> result = new ArrayList<TracedNode>();
		for (TracedNode nodeMapped : tracedNodes) {
			result.add(nodeMapped);
		}
		return result;
	}
	
	public void updateTrace(Node from, Node to, NodeProperty discriminatorProperty, Object discriminatorValue) {
		for (TracedNode tracedNode : tracedNodes) {
			if(tracedNode.isTracedNodeById(from) ) {
				tracedNode.updateTraceNode(from, to, discriminatorProperty, discriminatorValue);
			}
		}
	}
	
	public boolean existsNode(Node node) {
		for (TracedNode tracedNode : tracedNodes) {
			if(tracedNode.isTracedNodeById(node) ) {
				return true;
			}
		}
		return false;
	}
	
	public void addNodeGeneratedFromMultivaluedProperty(Node targetNode, Node newNode, NodeProperty propertyAffected) {
		if(!existsNode(targetNode))
			return;
		
		TracedNode newTracedNode = new TracedNode(newNode);
		TracedNode currentTracedNode = null;
		Filter propertyAffectedFilter = null;
		
		for (TracedNode nodeMapped : tracedNodes) {
			if(nodeMapped.getNodeMapped().isMyId(targetNode.getID()) ) {
				currentTracedNode = nodeMapped;
			}
		}
		
		for(Filter filter : currentTracedNode.getFilters()) {
			if(filter.getFilterProperty() == propertyAffected) {
				propertyAffectedFilter = filter;
			}
		}
		 
		if(propertyAffectedFilter != null) {
			currentTracedNode.removeFilterOfProperty(propertyAffected);
			newTracedNode.addFilter(propertyAffectedFilter);
		}
		tracedNodes.add(newTracedNode);
	}
	
//	public void addIntermediateNode(Node node1, Node node2, Node newNode) {
//		int pos1 = getNodePosition(node1);
//		int pos2 = getNodePosition(node2);
//		
//		if( pos1 != -1 && pos2 != -1) {
//			if(pos1 < pos2)
//				tracedNodes.add(pos1+1, new TracedNode(newNode, null, null));
//			else tracedNodes.add(pos2+1, new TracedNode(newNode, null, null));
//		}
//	}
	
//	public int getNodePosition(Node node) {
//		int position = 0;
//		for (TracedNode tracedNode : tracedNodes) {
//			if(tracedNode.getNodeMapped().getID().equals(node.getID()) ) {
//				return position;
//			}
//			position++;
//		}
//		return -1;
//	}
	
	public void addIntermediateNode(Node newNode, Node afterNode) {
		boolean putNode = false;
		int index = 0;
		if(!existsNode(newNode)) {
			
			while( !putNode && index < this.tracedNodes.size()) {
				if(this.tracedNodes.get(index).getNodeMapped().isMyId(afterNode.getID())) {
					this.tracedNodes.add (index+1, new TracedNode(newNode, null, null));
					putNode = true;
				}
				index ++;
			}
			if(!putNode) {
				tracedNodes.add(new TracedNode(newNode, null, null));
			}
//			tracedNodes.add(new TracedNode(newNode, null, null));
		}
	}
	
	public boolean hasFilter() {
		for (TracedNode tracedNode : tracedNodes) {
			if(tracedNode.hasFilter() ) {
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Filter> getFilters(){
		ArrayList<Filter> filters = new ArrayList<Filter>();
		for (TracedNode tracedNode : tracedNodes) {
			for(Filter filter : tracedNode.getFilters()) {
				filters.add(filter);
			}
		}
		return filters;
	}
	
	public void addFilters(ArrayList<Filter> filters, Node toNode) {
		for (TracedNode tracedNode : tracedNodes) {
			if(tracedNode.getNodeMapped().isMyId(toNode.getID())) {
				for(Filter filter : filters) {
					tracedNode.addFilter(filter);
				}
			}
		}
	}
	
	public void addFiltersForAllTracedNodes(ArrayList<Filter> filters) {
		for (TracedNode tracedNode : tracedNodes) {
			for(Filter filter : filters) {
				tracedNode.addFilter(filter);
			}
		}
	}
	
	/**
	 * Returns NodeMapped given the ID of a Node.
	 * @param id
	 * @return
	 */
	public Node getNodeMappedById(String nodeId) {
		for (TracedNode tracedNode : tracedNodes) {
			if(tracedNode.getNodeMapped().getID().equals(nodeId) ) {
				return tracedNode.getNodeMapped();
			}
		}
		return null;
	}
	
	/**
	 * Returns the main node of target nodes. The main node is the first node added.
	 * 
	 * @returns
	 */
	public Node getMainNode() {
		return this.tracedNodes.get(0).getNodeMapped();
	}
	
	/**
	 * Returns the main NodeMapped of targer nodes. The main node is the first node added.
	 * 
	 * @returns
	 */
	public TracedNode getMainNodeMapped() {
		return this.tracedNodes.get(0);
	}
	
	/**
	 * Returns the node of the given property ID.
	 *
	 * @param id
	 * @returns
	 */
	public Node getNodeByPropertyOriginalId(String id) {
		for (TracedNode tracedNode : this.tracedNodes ) {
			for (NodeProperty property : tracedNode.getNodeMapped().getProperties()) {
				if (property.getOriginalId().equals(id)) {
					return  tracedNode.getNodeMapped();
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the FKs properties of the main node. The main node is the first node
	 * added.
	 * 
	 * @returns
	 */
	public ArrayList<NodeProperty> getFKPropertiesOfMainNode() {
		ArrayList<NodeProperty> array = new ArrayList<NodeProperty>();
		Node node = this.getMainNode();
		for (NodeProperty property : node.getProperties()) {
			if (property.isForeignKey()) {
				array.add(property);
			}
		}
		return array;
	}
	
	/**
	 * Returns the property of the given ID.
	 *
	 * @param id
	 * @returns
	 */
	public NodeProperty getPropertyByOriginalId(String id) {
		for (TracedNode tracedNode : this.tracedNodes ) {
			for (NodeProperty property : tracedNode.getNodeMapped().getProperties()) {
				if (property.getOriginalId().equals(id)) {
					return property;
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the property of the given ID.
	 *
	 * @param id
	 * @returns
	 */
	public NodeProperty getPropertyById(String originalId) {
		for (TracedNode tracedNode : this.tracedNodes ) {
			for (NodeProperty property : tracedNode.getNodeMapped().getProperties()) {
				if (property.getID().equals(originalId)) {
					return property;
				}
			}
		}
		return null;
	}
	
	public void changeFiltersFromTo(Node from, Node to, boolean disassociateNodeFrom) {
		TracedNode fromNodeMapped = getNodeMappedFor(from);
		TracedNode toNodeMapped = getNodeMappedFor(to);
		
		//remove apenas se o nodo de origem existir no rastro
		if(fromNodeMapped != null) {
			for(Filter filter : fromNodeMapped.getFilters()) {
				//toNodeMapped.addFilter(filter.getFilterProperty(), filter.getValue(), filter.getMandatoryProperties());  Process restructured from version V0.2.
				toNodeMapped.addFilter(filter.getFilterProperty(), filter.getValue());
			}
			removeNodeMapped(from);
		}
	}
	
	public void removeNodeMapped(Node node) {
		int i = 0;
		while( i < this.tracedNodes.size() ) {
			if( tracedNodes.get(i).getNodeMapped().isMyId(node.getID()) ) {
				tracedNodes.remove(i);
			}
			else i++;
		}
	}
	
	private TracedNode getNodeMappedFor(Node node) {
		for (TracedNode tracedNode : tracedNodes) {
			if(tracedNode.getNodeMapped().isMyId(node.getID()))
				return tracedNode;
		}
		return null;
	}
//	Process restructured from version V0.2.
//	public boolean hasMandatoryProperties() {
//		for (TracedNode tracedNode : tracedNodes) {
//			if(tracedNode.hasMandatoryProperties())
//				return true;
//		}
//		return false;
//	}
	
	public ArrayList<NodeProperty> getMandatoryProperteOf(NodeProperty discriminatorProperty){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
		for (TracedNode tracedNode : tracedNodes) {
			mandatoryProperties = tracedNode.getMandatoryProperteOf(discriminatorProperty);
			for(NodeProperty property : mandatoryProperties) {
				result.add(property);
			}
		}
		return result;
	}

	public String toString() {
		String msg = "";
		boolean first = true;
		for (TracedNode nodeMapped : this.tracedNodes) {
			if(first) {
				msg += "TRACE: " + nodeMapped.toString();
				first = false;
			}
			else {
				msg += " | " + nodeMapped.toString();
			}
		}
		return msg;
	}

}

