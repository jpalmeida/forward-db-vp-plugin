package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.Statistic;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Filter;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TracedNode;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.Origin;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class MySqlMC6 {

	private TraceTable traceTable;

	public MySqlMC6(TraceTable traceTable) {
		this.traceTable = traceTable;
	}
	
	public String getRestrictions(Node node) {
		StringBuilder text = new StringBuilder();
		
		ArrayList <Trace> traces = new ArrayList<Trace>();
		ArrayList <Trace> tracesUsed = new ArrayList<Trace>();
		
		for (NodeProperty property : node.getProperties()) {
			if(isForeignKeyToValidade(property)) {
				traces = getTargetNodesBelongToForeignKey(property);
				for(Trace trace : traces) {
					if(!isTraceUsed(tracesUsed, trace))
						text.append(getConstraintToFk(property, trace));
					putTracesUsed(tracesUsed, trace);
				}
				
			}
		}
		return text.toString();
	}
	
	private boolean isForeignKeyToValidade(NodeProperty property) {
		String id;
		Node node;
		ArrayList <Trace> traces = new ArrayList<Trace>();
		boolean existsFilter = false;
		
		if(property.isForeignKey()) {
			//Checks if the table referenced by the foreign key exists in the original model. 
			//If it does not exist, is because it was created in the transformation process 
			//(from an ENUMbor multivalued attribute)
			id = property.getForeignKeyNodeID();
			node = traceTable.getOriginalGraph().getNodeById(id);
			if(node == null) {
				return false;
			}
			
			//Checks if the table of the foreign key exists in the original model. 
			//If it does not exist, is because it was created in the transformation process 
			//(from an N:N association)
//			id = property.getOwnerNode().getID();
//			node = traceTable.getOriginalGraph().getNodeById(id);
//			if(node == null) {
//				return false;
//			}
			
			//The absence of filters means that it comes from a flattening or there were no 
			//changes in the original model class, so there will be nothing to validate. 
			//Referential integrity resolves.
			traces = getTargetNodesBelongToForeignKey(property);
			for(Trace trace : traces) {
				if(trace.hasFilter())
					existsFilter = true;
			}
			
			if(!existsFilter)
				return false;
			
			return true;
		}
		return false;
	}
	
	private ArrayList<Trace> getTargetNodesBelongToForeignKey(NodeProperty property){
		ArrayList<Trace> traces = new ArrayList<Trace>();
		
		Graph originalGraph = traceTable.getOriginalGraph();
		Node referencedNode = originalGraph.getNodeById(property.getForeignKeyNodeID());
		TraceSet traceSet;
		GraphAssociation association;
		Node relatedNode;
		Node node;
		
		GraphAssociation originalAssociation = property.getAssociationRelatedOfFK().getOriginalAssociation();
		if(originalAssociation == null) {
			//This happens when the association is not destroyed in the transformation
			originalAssociation = originalGraph.getAssociationByID(property.getAssociationRelatedOfFK().getID());
		}

		if(traceTable.isSourceNodeMapped(property.getOwnerNode())) {
			if(originalAssociation.getTargetNode().isMyId(property.getOwnerNode().getID())) 
				node = originalAssociation.getSourceNode();
			else node = originalAssociation.getTargetNode();
			
			traceSet = traceTable.getTraceSetOf(node);
			for(Trace trace : traceSet.getTraces()) {
				if(trace.existsNode(referencedNode)) {
					traces.add(trace);
				}
			}
		}
		else {
			// is a intermediate node of N:N association
			
			traceSet = traceTable.getTraceSetOf(originalAssociation.getSourceNode());
			association = property.getAssociationRelatedOfFK();
			relatedNode = association.getNodeEndOf(property.getOwnerNode());
			
			for(Trace trace : traceSet.getTraces()) {
				if(trace.existsNode(property.getOwnerNode()) && trace.existsNode(relatedNode)) {
					traces.add(trace);
				}
			}
		}
		
		//Retrieve the traces that map to the foreign key source table.
		return traces;
	}
	
	private void putTracesUsed(ArrayList <Trace> tracesUsed, Trace newTrace) {
		tracesUsed.add(newTrace);
	}
	
	private boolean isTraceUsed(ArrayList <Trace> tracesUsed, Trace traceToUse) {
		for(Trace trace:  tracesUsed) {
			if(trace == traceToUse)
				return true;
		}
		
		return false;
	}
	
	private String getConstraintToFk(NodeProperty property, Trace trace) {
		String text = "";
		String tab = Util.getSpaces("", Util.getTabSpaces());
		GraphAssociation association = property.getAssociationRelatedOfFK().getOriginalAssociation();
		
		if(association == null)
			return "";
		
		if(property.isNullable()) {
			text += tab + "if( NEW."+ property.getName() + " is not null ) \n";
			text += tab + "then \n";
		}
		
		tab = Util.getSpaces("", Util.getTabSpaces() * 2);
		text += tab + "if not exists ( \n";
		
		tab = Util.getSpaces("", Util.getTabSpaces() * 5);
		
		text += tab + getSelect();
		text += tab + getFrom(trace,property);
		text += tab + getWhere(trace, property);
		
		text += Util.getSpaces("", 19) + ") \n";
		tab = Util.getSpaces("", Util.getTabSpaces());
		
		text += tab + "then \n";
				
		text += tab + tab + "set msg = 'ERROR: Violating conceptual model rules";
		text += "[XX_TRIGGER_NAME_XX].'; \n";
		text += tab + tab + "signal sqlstate '45000' set message_text = msg;\n";
		
		tab = Util.getSpaces("", Util.getTabSpaces() * 2);
		text += tab + "end if; \n\n";
		
		if(property.isNullable()) {
			tab = Util.getSpaces("", Util.getTabSpaces());
			text += tab + "end if; \n\n";
		}
		
		Statistic.addMC6();
		
		return text;
	}
	
	private String getSelect() {
		return "SELECT 1\n";
	}
	
	private String getFrom(Trace trace, NodeProperty fkProperty) {
		StringBuilder text = new StringBuilder();
		String joinText;
		Node fromNode, toNode = null;
		TracedNode toTracedNode;
		
		setUnsolvedNodes(trace);
		fromNode = getNextTracedNodeToJoin(trace).getNodeMapped();
		
		text.append("FROM ");
		text.append(fromNode.getName());
		text.append(" ");
		
		while(existsNodeNotResolved(trace)) {
			toTracedNode = getNextTracedNodeToJoin(trace);
			toNode = toTracedNode.getNodeMapped();
			
			joinText = getJoin(fromNode, toNode, trace);
			
			if(joinText != null) {
				text.append(joinText);
			}
			
			text.append( getFilter(toTracedNode) );
			
			fromNode = toNode;
		}
		
		text.append("\n");
		
		return text.toString();
	}
	
	private String getWhere(Trace trace, NodeProperty fKProperty) {
		Node node;
		boolean first = true;
		String text = "";
		String tab = Util.getSpaces("", Util.getTabSpaces() * 5);
		
		text += "WHERE ";

		for (Filter filter : trace.getMainNodeMapped().getFilters()) {

			if (first) {
				first = false;
			} else {
				text += tab;
				text += "AND   ";
			}

			text += filter.getFilterProperty().getName();
			text += " = ";
			text += Util.getStringValue(filter.getValue());
			text += " \n";
		}
		
		if( !first )
			text += tab + "AND   ";
		
		node = trace.getNodeMappedById(fKProperty.getForeignKeyNodeID());
		
		text += node.getName();
		text += ".";
		text += node.getPKName();
		text += " = ";
		text += "NEW."+ fKProperty.getName();
		text += "\n";
		
		return text;
	}
	
	private void setUnsolvedNodes(Trace trace) {
		Node node;
		for(TracedNode tracedNode : trace.getTracedNodes()) {
			node = tracedNode.getNodeMapped();
			if(node.getOrigin() == Origin.N2NASSOCIATION)
				node.setResolved(true);
			else node.setResolved(false);
		}
	}
	
	private boolean existsNodeNotResolved(Trace trace) {
		for(TracedNode tracedNode : trace.getTracedNodes()) {
			if(!tracedNode.getNodeMapped().isResolved())
				return true;
		}
		return false;
	}
	
	private TracedNode getNextTracedNodeToJoin(Trace trace) {
		Node node;
		for(TracedNode tracedNode : trace.getTracedNodes()) {
			node = tracedNode.getNodeMapped();
			if(!node.isResolved()) {
				node.setResolved(true);
				return tracedNode;
			}
		}
		return null;
	}
	
	private String getJoin(Node fromNode, Node toNode, Trace trace) {
		String text = "";
		String tab = Util.getSpaces("", Util.getTabSpaces() * 5);
		GraphAssociation association;
		Cardinality cardinalityEnd;
		
		text += "\n";
		text += tab;

		association = getAssociationBetweenNodes(fromNode, toNode, trace);
		cardinalityEnd = association.getCardinalityEndOf(fromNode);
		
		if(cardinalityEnd == Cardinality.C1 || cardinalityEnd == Cardinality.C1_N)
			text += "INNER JOIN ";
		else text += "LEFT JOIN ";
		
		text += toNode.getName();
		text += "\n";
		tab = Util.getSpaces("", Util.getTabSpaces() * 7);
		text += tab + "ON  ";

		if (toNode.getFKRelatedOfNodeID(fromNode.getID()) != null) {
			text += fromNode.getName();
			text += ".";
			text += fromNode.getPKName();
			text += " = ";
			text += toNode.getName();
			text += ".";
			text += toNode.getFKRelatedOfNodeID(fromNode.getID()).getName();
		} else {
			if (fromNode.getFKRelatedOfNodeID(toNode.getID()) != null) {
				text += fromNode.getName();
				text += ".";
				text += fromNode.getFKRelatedOfNodeID(toNode.getID()).getName();
				text += " = ";
				text += toNode.getName();
				text += ".";
				text += toNode.getPKName();
			} else {
				// try establish the joins with another node of the trace
				fromNode = getNextTracedNodeToJoin(trace).getNodeMapped();
				if(fromNode != null) {
					if (toNode.getFKRelatedOfNodeID(fromNode.getID()) != null) {
						text += fromNode.getName();
						text += ".";
						text += fromNode.getPKName();
						text += " = ";
						text += toNode.getName();
						text += ".";
						text += toNode.getFKRelatedOfNodeID(fromNode.getID()).getName();
					} else {
						return null;
					}
				}
				else {
					return null;
				}
			}
		}
		
		return text;
	}
	
	private String getFilter(TracedNode nodeMapped) {
		String text = "";
		String tab = Util.getSpaces("", Util.getTabSpaces() * 8);

		for (Filter filter : nodeMapped.getFilters()) {
			text += "\n";
			text += tab;
			text += "AND ";
			text += nodeMapped.getNodeMapped().getName();
			text += ".";
			text += filter.getFilterProperty().getName();
			text += " = ";
			text += Util.getStringValue(filter.getValue());
			text += " ";
		}
		return text;
	}
	
	private GraphAssociation getAssociationBetweenNodes(Node fromNode, Node toNode, Trace trace) {
		for(GraphAssociation fromAssociation : fromNode.getAssociations()) {
			if(fromAssociation.getNodeEndOf(fromNode).isMyId(toNode.getID())) {
				return fromAssociation;
			}
		}
		
		for(TracedNode tracedNode : trace.getTracedNodes()) {
			fromNode = tracedNode.getNodeMapped();
			for(GraphAssociation fromAssociation : fromNode.getAssociations()) {
				if(fromAssociation.getNodeEndOf(fromNode).isMyId(toNode.getID())) {
					return fromAssociation;
				}
			}
		}
		
		return null;
	}
}
