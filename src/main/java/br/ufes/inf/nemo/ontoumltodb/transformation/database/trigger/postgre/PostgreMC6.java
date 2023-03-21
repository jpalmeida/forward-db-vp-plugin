package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.postgre;

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

public class PostgreMC6 {
	private TraceTable traceTable;

	public PostgreMC6(TraceTable traceTable) {
		this.traceTable = traceTable;
	}
	
	public String getRestrictions(Node node) {
		StringBuilder text = new StringBuilder();
		ArrayList <Trace> traces = new ArrayList<Trace>();
		
		for (NodeProperty property : node.getForeignKeys()) {
			if(isForeignKeyToValidade(property)) {
				traces = getTargetNodesBelongToForeignKey(property);
				for(Trace trace : traces) {
					text.append(getConstraintToFk(property, trace));
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
		text.append("RAISE EXCEPTION  'ERROR 4: Violating conceptual model rules [XX_TRIGGER_NAME_XX].'; \n");

		text.append(tab2);
		text.append("end if; \n\n");
		
		
		text.append(tab1);
		text.append("end if; \n\n");
		
		Statistic.addMC6();
		
		return text.toString();
	}
	
	private String getSelect() {
		return "select 1\n";
	}
	
	private String getFrom(Trace trace, NodeProperty fkProperty, String currentTab) {
		StringBuilder text = new StringBuilder();
		String joinText;
		GraphAssociation association = null;
		ArrayList<Node> nodesUsed = new ArrayList<Node>();
		Node nodeFrom, nodeTo;
		
		ArrayList<GraphAssociation> allAssociations = getAssociationsOfTracedNodes(trace);
		
		nodeFrom = trace.getMainNodeMapped().getNodeMapped();
		nodesUsed.add(nodeFrom);
		
		text.append("from ");
		text.append(nodeFrom.getName());
		text.append(" ");
		
		while( (association = getNextAssociation(allAssociations) )!= null ) {
			if( isNodeUsed(association.getSourceNode(), nodesUsed)) {
				nodeFrom = association.getSourceNode();
				nodeTo = association.getTargetNode();
			}
			else {
				nodeFrom = association.getTargetNode();
				nodeTo = association.getSourceNode();
			}
			nodesUsed.add(nodeTo);
			
			joinText = getJoin(nodeFrom, nodeTo, association, trace, currentTab);
			if(joinText != null) {
				text.append(joinText);
			}
		}
		text.append("\n");
		
		return text.toString();
	}
	
	private ArrayList<GraphAssociation> getAssociationsOfTracedNodes(Trace trace){
		ArrayList<GraphAssociation> result = new ArrayList<GraphAssociation>();
		ArrayList<Node> nodesTraced = new ArrayList<Node>();
		int index = 1; // start from second traced node.
		Node node;
		boolean exists = false;
		
		nodesTraced.add(trace.getMainNode()); // the first node must participate of select
		
		// get the nodes to do the joins
		for(TracedNode tracedNode : trace.getTracedNodes()) {
			if(tracedNode.getNodeMapped().getOrigin() != Origin.N2NASSOCIATION) {
				if(tracedNode.hasFilter()) {
					if(!hasNode(tracedNode.getNodeMapped(), nodesTraced))
						nodesTraced.add(tracedNode.getNodeMapped());
				}
			}
		}
		
		// get the associations between the nodes to do the joins
		while(index < nodesTraced.size()) {
			node = nodesTraced.get(index);
			for(GraphAssociation association : node.getAssociations()) {
				exists = false;
				if(association.getSourceNode().getName().equals(node.getName() ) )
					exists = hasDestinationNode(association.getTargetNode(), nodesTraced);
				else exists  = hasDestinationNode(association.getSourceNode(), nodesTraced);
				
				if(exists) {
					if(!hasAssociation(association, result)) {
						association.setResolved(false);
						result.add(association);
					}
				}
			}
			index++;
		}
		
		return result;
	}
	
	private boolean hasDestinationNode(Node evaluatedNode, ArrayList<Node> nodesTraced) {
		for(Node node : nodesTraced) {
			if(node.getName().equals(evaluatedNode.getName()))
				return true;
		}
		return false;
	}
	
	private boolean hasNode(Node evaluatedNode, ArrayList<Node> nodesTraced) {
		for(Node node : nodesTraced) {
			if(evaluatedNode.getName().equals(node.getName()))
				return true;
		}
		return false;
	}
	
	private boolean hasAssociation(GraphAssociation evaluatedAssociation, ArrayList<GraphAssociation> result) {
		
		for(GraphAssociation association : result) {
			if(evaluatedAssociation.isMyId(association.getID()))
				return true;
		}
		return false;
	}
	
	private GraphAssociation getNextAssociation(ArrayList<GraphAssociation> allAssociations) {
		for(GraphAssociation association : allAssociations) {
			if(association.isResolved() == false) {
				association.setResolved(true);
				return association;
			}
		}
		return null;
	}
	
	private boolean isNodeUsed(Node evaluatedNode, ArrayList<Node> nodesUsed) {
		for(Node node : nodesUsed) {
			if(evaluatedNode.getName().equals(node.getName())) {
				return true;
			}
		}
		return false;
	}
	
	private String getJoin(Node nodeFrom, Node nodeTo, GraphAssociation association, Trace trace, String currentTab) {
		StringBuilder text = new StringBuilder();
		String tab2 = currentTab + Util.getSpaces("", Util.getTabSize() * 2);
		Cardinality cardinalityEnd;
		TracedNode tracedNodeTo;
		
		text.append("\n");
		text.append(currentTab);

		cardinalityEnd = association.getCardinalityEndOf(nodeFrom);
		
		if(cardinalityEnd == Cardinality.C1 || cardinalityEnd == Cardinality.C1_N)
			text.append("inner join ");
		else text.append("left join ");
		
		text.append(nodeTo.getName());
		text.append("\n");
		
		text.append(tab2);
		text.append("on  ");

		if (nodeTo.getFKRelatedOfNodeID(nodeFrom.getID()) != null) {
			text.append(nodeFrom.getName());
			text.append(".");
			text.append(nodeFrom.getPKName());
			text.append(" = ");
			text.append(nodeTo.getName());
			text.append(".");
			text.append(nodeTo.getFKRelatedOfNodeID(nodeFrom.getID()).getName());
		} else {
			if (nodeFrom.getFKRelatedOfNodeID(nodeTo.getID()) != null) {
				text.append(nodeFrom.getName());
				text.append(".");
				text.append(nodeFrom.getFKRelatedOfNodeID(nodeTo.getID()).getName());
				text.append(" = ");
				text.append(nodeTo.getName());
				text.append(".");
				text.append(nodeTo.getPKName());
			}
		}
		
		tracedNodeTo = trace.getTracedNodeFor(nodeTo);
		text.append( getFilter(tracedNodeTo, tab2) );
		
		return text.toString();
	}
	
	private String getFilter(TracedNode nodeMapped, String currentTab) {
		String text = "";

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

}
