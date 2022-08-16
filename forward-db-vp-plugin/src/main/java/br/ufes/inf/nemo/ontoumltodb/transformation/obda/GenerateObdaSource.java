package br.ufes.inf.nemo.ontoumltodb.transformation.obda;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Filter;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TracedNode;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;
import br.ufes.inf.nemo.ontoumltodb.util.NodeOrigin;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class GenerateObdaSource {

	public static String generate(Node sourceNode, Trace trace) {
		String text = "";

		text += "source       ";
		text += getSelect(sourceNode, trace);
		text += getFrom(sourceNode, trace);
		text += getWhere(sourceNode, trace);

		return text;
	}

	private static String getSelect(Node sourceNode, Trace trace) {
		String text = "";

		// In the source node there is no PK, however it must be added to the query.
		text += "SELECT ";
		text += trace.getMainNode().getName();
		text += ".";
		text += trace.getMainNode().getPKName();

		for (NodeProperty sourceProperty : sourceNode.getProperties()) {
			text += ", ";
			text += trace.getNodeByPropertyOriginalId(sourceProperty.getOriginalId()).getName();
			text += ".";
			text += trace.getPropertyByOriginalId(sourceProperty.getID()).getName();
		}

		// put the foreign keys
		for (NodeProperty property : trace.getFKPropertiesOfMainNode()) {
			text += ", ";
			text += trace.getMainNode().getName();
			text += ".";
			text += property.getName();
		}
		text += " \n";

		return text;
	}

	private static String getFrom(Node sourceNode, Trace trace) {
		String text = "";
		String smallTab;
		String largeTab;
		boolean first = true;
		Node lastNode = null, node;

		smallTab = Util.getSpaces("", 13);
		largeTab = Util.getSpaces("", 20);

		text += smallTab;
		first = true;

		for (TracedNode nodeMapped : trace.getTracedNodes()) {
			node = nodeMapped.getNodeMapped();
			if (first) {
				text += "FROM ";
				text += node.getName();
				text += " ";
				first = false;
				lastNode = node;
			} else {
				if(existsColumnToShowInJoinedTabels(sourceNode, trace) || existsFilterInJoinedTables(trace)) {
					text += getJoin(smallTab, largeTab, lastNode, nodeMapped);
					text += " ";
					lastNode = node;
				}
			}
		}
		text += "\n";

		return text;
	}
	
	private static boolean existsColumnToShowInJoinedTabels(Node sourceNode, Trace trace) {
		TracedNode tracedNode;
		
		if(trace.getTracedNodes().size() == 1)
			return false;
		
		for(int i = 1; i < trace.getTracedNodes().size(); i++) {
			tracedNode = trace.getTracedNodes().get(i);
			
			
			for(NodeProperty sourceProperty : sourceNode.getProperties()) {
				
				for(NodeProperty property : tracedNode.getNodeMapped().getProperties()) {
					if(sourceProperty.isMyId(property.getOriginalId()))
						return true;
				}
			}
		}
		
		return false;
	}
	
	private static boolean existsFilterInJoinedTables(Trace trace) {
		TracedNode tracedNode;
		
		if(trace.getTracedNodes().size() == 1)
			return false;
		
		for(int i = 1; i < trace.getTracedNodes().size(); i++) {
			tracedNode = trace.getTracedNodes().get(i);
			
			if(tracedNode.getFilters().size() > 0)
				return true;
		}
		
		return false;
	}
	
	private static String getJoin(String smallTab, String largeTab, Node lastNode, TracedNode tracedNode) {
		Node currentNode = tracedNode.getNodeMapped();
		String text = "";
		text += "\n";
		text += smallTab;
		
		//alterar para verificar a cardinalidade da associação!!!!!!!!
		if(currentNode.getNodeOrigin() == NodeOrigin.FROM_ATTRIBUTE)
			text += "LEFT JOIN ";
		else text += "INNER JOIN ";
		
		text += currentNode.getName();
		text += "\n";
		text += largeTab;
		text += "ON ";

		if (currentNode.getFKRelatedOfNodeID(lastNode.getID()) != null) {
			text += lastNode.getName();
			text += ".";
			text += lastNode.getPKName();
			text += " = ";
			text += currentNode.getName();
			text += ".";
			text += currentNode.getFKRelatedOfNodeID(lastNode.getID()).getName();
		} else {
			if (lastNode.getFKRelatedOfNodeID(currentNode.getID()) != null) {
				text += lastNode.getName();
				text += ".";
				text += lastNode.getFKRelatedOfNodeID(currentNode.getID()).getName();
				text += " = ";
				text += currentNode.getName();
				text += ".";
				text += currentNode.getPKName();
			} else {
				text += "Is not possible to establish the join between the " + lastNode.getName() + " and "
						+ currentNode.getName() + " tables.";
			}
		}
		text += getFilter(largeTab, tracedNode);
		return text;
	}

	private static String getFilter(String largeTab, TracedNode tracedNode) {
		String text = "";

		for (Filter filter : tracedNode.getFilters()) {
			text += "\n";
			text += largeTab;
			text += "AND ";
			text += tracedNode.getNodeMapped().getName();
			text += ".";
			text += filter.getFilterProperty().getName();
			text += " = ";
			text += getStringValue(filter.getValue());
			text += " ";
		}
		return text;
	}

	private static String getWhere(Node sourceNode, Trace trace) {
		boolean first = true;
		String text = "";
		String smallTab = Util.getSpaces("", 13);

		for (Filter filter : trace.getMainNodeMapped().getFilters()) {

			if (first) {
				text += smallTab;
				text += "WHERE ";
				first = false;
			} else {
				text += smallTab;
				text += "AND   ";
			}

			text += filter.getFilterProperty().getName();
			text += " = ";
			text += getStringValue(filter.getValue());
			text += " \n";
		}
		return text;
	}

	private static String getStringValue(Object value) {
		String text = value.toString();
		text = text.toString().toUpperCase();

		if (value instanceof String) {
			return "'" + text + "'";
		} else {
			return text;
		}
	}
}
