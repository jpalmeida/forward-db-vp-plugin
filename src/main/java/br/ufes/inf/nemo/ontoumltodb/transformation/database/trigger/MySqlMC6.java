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
		
		text.append(getMC6(node));
		// DO NOT REMOVE THIS CODE (GETMC6INVERSE) !!!!!!!!!!!!!!!!!!!!!!!!!!!!
//		text.append(getMC6Inverse(node));
		
		return text.toString();
	}	
	
	private String getMC6(Node node) {
		StringBuilder text = new StringBuilder();
		ArrayList <Trace> traces = new ArrayList<Trace>();
		
		for (NodeProperty property : node.getProperties()) {
			if(isForeignKeyToValidade(property)) {
				traces = getTargetNodesBelongToForeignKey(property);
				for(Trace trace : traces) {
					text.append(getConstraintToFk(property, trace));
				}
				
			}
		}
		
		return text.toString();
	}
	
	// DO NOT REMOVE THIS CODE (GETMC6INVERSE) !!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	private String getMC6Inverse(Node node) {
//		StringBuilder text = new StringBuilder();
//		Trace trace;
//		
//		for(MissingConstraintData mc : node.getMissingConstraint(MissingConstraint.MC6_Inverse)) {
//			trace = traceTable.getTracesById(mc.getSourceNode()).get(0);
//			for(NodeProperty fk : node.getForeignKeys()) {
//				if(fk.getAssociationRelatedOfFK().getOriginalAssociation().isMyId(mc.getSourceAssociation().getOriginalAssociation().getID()))
//					text.append(getInverseConstraintToFk(fk, trace, node));
//			}
//		}
//		return text.toString();
//	}
	
	
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
		GraphAssociation originalAssociation;
		Node relatedNode;
		Node node;
		
		association = property.getAssociationRelatedOfFK();
		originalAssociation = property.getAssociationRelatedOfFK().getOriginalAssociation();
		
		if(originalAssociation == null) {
			//This happens when the association is not destroyed in the transformation
			originalAssociation = originalGraph.getAssociationByID(property.getAssociationRelatedOfFK().getID());
		}

		if(property.getOwnerNode().getOrigin() != Origin.N2NASSOCIATION) {
			
			if( isSourceNodeToEvaluate(association))
				node = originalAssociation.getSourceNode();
			else node = originalAssociation.getTargetNode();
			
//			if( Util.isLowCartinality(association.getSourceCardinality()) &&  Util.isHightCartinality(association.getTargetCardinality()))
//				node = originalAssociation.getSourceNode();
//			else node = originalAssociation.getTargetNode();
			
			traceSet = traceTable.getTraceSetById(node);
			for(Trace trace : traceSet.getTraces()) {
				if(trace.existsNode(referencedNode)) {
					traces.add(trace);
				}
			}
		}
		else {
			traceSet = traceTable.getTraceSetById(originalAssociation.getSourceNode());
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
	
	private boolean isSourceNodeToEvaluate(GraphAssociation association) {
		
		if(		(association.getSourceCardinality() == Cardinality.C0_1 && association.getTargetCardinality() == Cardinality.C0_N) ||
				(association.getSourceCardinality() == Cardinality.C0_1 && association.getTargetCardinality() == Cardinality.C1_N) ||
				(association.getSourceCardinality() == Cardinality.C1   && association.getTargetCardinality() == Cardinality.C0_N) ||
				(association.getSourceCardinality() == Cardinality.C1   && association.getTargetCardinality() == Cardinality.C1_N) ||
				(association.getSourceCardinality() == Cardinality.C1   && association.getTargetCardinality() == Cardinality.C0_1)   
		)
			return true;
		else return false;
	}
	
	private String getConstraintToFk(NodeProperty property, Trace trace) {
		StringBuilder text = new StringBuilder();
		String tab1 = Util.getSpaces("", Util.getTabSize());
		String tab2 = Util.getSpaces("", Util.getTabSize() * 2);
		String tab3 = Util.getSpaces("", Util.getTabSize() * 3);
		String tab5 = Util.getSpaces("", Util.getTabSize() * 5);
		
		text.append(tab1);
		text.append("if( new.");
		text.append(property.getName());
		text.append(" is not null ) \n");
		text.append(tab1);
		text.append("then \n");
	
		
		text.append(tab2);
		text.append("if not exists ( \n");
		
		text.append(tab5);
		text.append(getSelect());
		text.append(tab5);
		text.append(getFrom(trace,property, tab5));
		text.append(tab5);
		text.append(getWhere(trace, property));
		
		text.append(tab2);
		text.append(") \n");
		
		text.append(tab2);
		text.append("then \n");
		
		text.append(tab3);
		text.append("set msg = 'ERROR: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n");
		text.append(tab3);
		text.append("signal sqlstate '45000' set message_text = msg;\n");
		
		
		text.append(tab2);
		text.append("end if; \n\n");
		
		
		text.append(tab1);
		text.append("end if; \n\n");
		
		Statistic.addMC6();
		
		return text.toString();
	}
	
	
	// DO NOT REMOVE THIS CODE (GETMC6INVERSE) !!!!!!!!!!!!!!!!!!!!!!!!!!!!
//	private String getInverseConstraintToFk(NodeProperty property, Trace trace, Node node) {
//		StringBuilder text = new StringBuilder();
//		String tab = Util.getSpaces("", Util.getTabSize());
//		String tab2 = Util.getSpaces("", Util.getTabSize()*2);
//		boolean first = true;
//		boolean existsFilter = false;
//		
//		if(trace.getMainNodeMapped().getFilters().isEmpty())
//			return "";
//		
//		text.append(tab);
//		text.append("if( NEW.");
//		text.append(property.getName());
//		text.append(" is not null AND (");
//		
//		for (Filter filter : trace.getMainNodeMapped().getFilters()) {
//			
//			if(		node.existsPropertyName(property.getName())&&
//					node.existsPropertyName(filter.getFilterProperty().getName()) 
//			) {
//				existsFilter = true; 
//				if (first) {
//					first = false;
//				} else {
//					text.append(tab);
//					text.append(" OR ");
//				}
//	
//				text.append("NEW.");
//				text.append(filter.getFilterProperty().getName());
//				text.append(" <> ");
//				text.append(Util.getStringValue(filter.getValue()));
//			}
//		}
//		
//		if(!existsFilter)
//			return "";
//		
//		text.append(")");
//		text.append(" \n");
//		text.append(tab);
//		text.append(") \n");
//		text.append(tab);
//		text.append("then \n");
//				
//		text.append(tab2);
//		text.append("set msg = 'ERROR: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n");
//		text.append(tab2);
//		text.append("signal sqlstate '45000' set message_text = msg;\n");
//		
//		text.append(tab);
//		text.append("end if; \n\n");
//		
//		Statistic.addMC6();
//		
//		return text.toString();
//	}
	
	private String getSelect() {
		return "select 1\n";
	}
	
	private String getFrom(Trace trace, NodeProperty fkProperty, String currentTab) {
		StringBuilder text = new StringBuilder();
		String joinText;
		Node fromNode, toNode = null;
		TracedNode toTracedNode;
		
		setUnsolvedNodes(trace);
		fromNode = getNextTracedNodeToJoin(trace).getNodeMapped();
		
		text.append("from ");
		text.append(fromNode.getName());
		text.append(" ");
		
		while(existsNodeNotResolved(trace)) {
			toTracedNode = getNextTracedNodeToJoin(trace);
			toNode = toTracedNode.getNodeMapped();
			
			joinText = getJoin(fromNode, toNode, trace, toTracedNode, currentTab);
			
			if(joinText != null) {
				text.append(joinText);
			}
			
			fromNode = toNode;
		}
		
		text.append("\n");
		
		return text.toString();
	}
	
	private String getWhere(Trace trace, NodeProperty fKProperty) {
		Node node;
		boolean first = true;
		String text = "";
		String tab = Util.getSpaces("", Util.getTabSize() * 5);
		
		text += "where ";

		for (Filter filter : trace.getMainNodeMapped().getFilters()) {

			if (first) {
				first = false;
			} else {
				text += tab;
				text += "and   ";
			}

			text += filter.getFilterProperty().getName();
			text += " = ";
			text += Util.getStringValue(filter.getValue());
			text += " \n";
		}
		
		if( !first )
			text += tab + "and   ";
		
		node = trace.getNodeMappedById(fKProperty.getForeignKeyNodeID());
		
		text += node.getName();
		text += ".";
		text += node.getPKName();
		text += " = ";
		text += "new."+ fKProperty.getName();
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
	
	private String getJoin(Node fromNode, Node toNode, Trace trace, TracedNode toTracedNode, String currentTab) {
		StringBuilder text = new StringBuilder();;
		String tab2 = currentTab + Util.getSpaces("", Util.getTabSize() * 2);
		GraphAssociation association;
		Cardinality cardinalityEnd;
		
		text.append("\n");
		text.append(currentTab);

		association = getAssociationBetweenNodes(fromNode, toNode, trace);
		cardinalityEnd = association.getCardinalityEndOf(fromNode);
		
		if(cardinalityEnd == Cardinality.C1 || cardinalityEnd == Cardinality.C1_N)
			text.append("inner join ");
		else text.append("left join ");
		
		text.append(toNode.getName());
		text.append("\n");
		
		text.append(tab2);
		text.append("on  ");

		if (toNode.getFKRelatedOfNodeID(fromNode.getID()) != null) {
			text.append(fromNode.getName());
			text.append(".");
			text.append(fromNode.getPKName());
			text.append(" = ");
			text.append(toNode.getName());
			text.append(".");
			text.append(toNode.getFKRelatedOfNodeID(fromNode.getID()).getName());
		} else {
			if (fromNode.getFKRelatedOfNodeID(toNode.getID()) != null) {
				text.append(fromNode.getName());
				text.append(".");
				text.append(fromNode.getFKRelatedOfNodeID(toNode.getID()).getName());
				text.append(" = ");
				text.append(toNode.getName());
				text.append(".");
				text.append(toNode.getPKName());
			} else {
				// try establish the joins with another node of the trace
				fromNode = getNextTracedNodeToJoin(trace).getNodeMapped();
				if(fromNode != null) {
					if (toNode.getFKRelatedOfNodeID(fromNode.getID()) != null) {
						text.append(fromNode.getName());
						text.append(".");
						text.append(fromNode.getPKName());
						text.append(" = ");
						text.append(toNode.getName());
						text.append(".");
						text.append(toNode.getFKRelatedOfNodeID(fromNode.getID()).getName());
					} else {
						return null;
					}
				}
				else {
					return null;
				}
			}
		}
		
		text.append( getFilter(toTracedNode, tab2) );
		
		return text.toString();
	}
	
	private String getFilter(TracedNode nodeMapped, String currentTab) {
		String text = "";
		//String tab = currentTab + Util.getSpaces("", Util.getTabSize());

		for (Filter filter : nodeMapped.getFilters()) {
			text += "\n";
			text += currentTab;
			text += "and ";
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

