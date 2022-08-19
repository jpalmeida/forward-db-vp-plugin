package br.ufes.inf.nemo.ontoumltodb.transformation.database.view;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TracedNode;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Filter;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public class TraceSetQueryBuilder {
	
	private Graph originalGraph;
	private TraceTable traceTable;
	private boolean putInheritedAttributes;
	
	public TraceSetQueryBuilder(Graph originalGraph, TraceTable traceTable, boolean putInheritedAttributes) {
		this.originalGraph = originalGraph;
		this.traceTable = traceTable;
		this.putInheritedAttributes = putInheritedAttributes;
	}
	
	public String generateQueryForTraceSet(TraceSet traceSet) {
		String text = "";
		QueryBuilder queryBuilder = null;
		boolean first = true;
		
		for(Trace trace : traceSet.getTraces()) {
			if(first) {
				first = false;
			}
			else {
				text += "\nUNION \n\n";
			}
			queryBuilder = getQueryForTrace(trace, traceSet.getSourceNode());
			text += queryBuilder.getQuery(); 
		}
		
		return text + ";\n";
	}
	
	public QueryBuilder getQueryForTrace(Trace trace, Node sourceNode) {
		QueryBuilder queryBuilder = new QueryBuilder();
		
		for(TracedNode tracedNode : trace.getTracedNodes()) {
			mountQueryForTracedNode(tracedNode, sourceNode, queryBuilder);
		}
		
		return queryBuilder;
	}
	
	private void mountQueryForTracedNode(TracedNode tracedNode, Node sourceNode, QueryBuilder queryBuilder) {
		if(putInheritedAttributes)
			putInheritedColumns(sourceNode, sourceNode, tracedNode, queryBuilder);
		else mountQuery(sourceNode, tracedNode, queryBuilder);
		
	}
	
	private void putInheritedColumns(Node sourceNode, Node evaluatedNode, TracedNode tracedNode, QueryBuilder queryBuilder) {
		TraceSet superTraceSet;
	
		for(Node node : traceTable.getOriginalGraph().getSuperNodes(evaluatedNode)) {
			superTraceSet = traceTable.getTraceSetById(node);
			for(Trace superTrace : superTraceSet.getTraces()) {
				if(checkSuperClass(sourceNode, superTrace)) {
					for(TracedNode superTracedNode : superTrace.getTracedNodes()) {
						putInheritedColumns(sourceNode, superTraceSet.getSourceNode(), superTracedNode, queryBuilder);
					}
				}
			}
		}
		// For put the superclass attributes first
		mountQuery(evaluatedNode, tracedNode, queryBuilder);
	}

	private boolean checkSuperClass(Node checkNode, Trace superTrace) {
		Node superNode = superTrace.getMainNode();
		superNode = traceTable.getOriginalGraph().getNodeById(superNode.getOriginalId());
		
		if(checkNode.isMyId(superNode.getID()))
			return true;
		else return traceTable.getOriginalGraph().isSubNodeOf(superNode, checkNode);
	}
	
	private void mountQuery(Node sourceNode, TracedNode tracedNode, QueryBuilder queryBuilder) {
		putPrimaryKey(sourceNode, tracedNode, queryBuilder);
		putSourceColumns(sourceNode, tracedNode, queryBuilder);
		putForeignKeys(sourceNode, tracedNode, queryBuilder);
		putFilters(tracedNode, queryBuilder);
	}
	
	private void putSourceColumns(Node sourceNode, TracedNode tracedNode, QueryBuilder queryBuilder) {
		String columnName;
		String nickname;
		
		for(NodeProperty sourceProperty : sourceNode.getProperties()) {
			if(tracedNode.existsOriginalPropertyInNodeMapped(sourceProperty)) {
				//columnName = sourceProperty.getOriginalName();
				columnName = tracedNode.getNodeMapped().getPropertyByOriginalName(sourceProperty.getName()).getName();
				nickname = sourceProperty.getName();
				//nickname = formatName(nickname);
				if(!queryBuilder.existsColumn(tracedNode.getNodeMapped().getName(), columnName, nickname)) {
					queryBuilder.addColumn(
							tracedNode.getNodeMapped(),
							columnName,
							nickname
							);
				}
			}
		}
	}
	
	private void putPrimaryKey(Node sourceNode, TracedNode tracedNode, QueryBuilder queryBuilder) {
		String columnName;
		String nickname;
		
		ArrayList<Node> rootNodes = originalGraph.getRootNodes(sourceNode.getID());
		
		for(Node rootNode : rootNodes) {
			columnName = tracedNode.getNodeMapped().getPKName();
			nickname = rootNode.getName() + "Id";
			//nickname = formatName(nickname);
			
			if(!queryBuilder.existsNickname(nickname) ) {
				queryBuilder.addColumn(
						tracedNode.getNodeMapped(),
						columnName,
						nickname
						);
			}
		}
	}
	
	private void putForeignKeys(Node sourceNode, TracedNode tracedNode, QueryBuilder queryBuilder) {
		String columnName;
		String nickname;
		
		for (NodeProperty property : tracedNode.getNodeMapped().getProperties()) {				
			if(property.isForeignKey() &&  !property.isPrimaryKey()) {
				
				columnName = property.getName();
				
				if(!queryBuilder.existsColumnName(columnName) ) {
					nickname = getNicknameForFK(property, tracedNode.getNodeMapped(), sourceNode);
					//nickname = formatName(nickname);
					queryBuilder.addColumn(
							tracedNode.getNodeMapped(),
							columnName,
							nickname
							);
				}
			}
			else {
				if(property.isForeignKey() &&  property.isPrimaryKey()) {
					queryBuilder.addTable(tracedNode.getNodeMapped());
				}
			}
		}
	}
	
	private String getNicknameForFK(NodeProperty property, Node node, Node sourceNode) {
		String nickname = "";
		
		if(existsSameNicknameForDifferentsProperties(property, node, sourceNode))
			nickname = getNicknameColision(property, node, sourceNode);
		else
			nickname = getCommonNicknameForFK(property, node, sourceNode);
		
		return nickname;
	}
	
	private boolean existsSameNicknameForDifferentsProperties(NodeProperty property, Node node, Node sourceNode) {
		GraphAssociation associationProperty, associationCurrent;
		String nicknameProperty, nicknameCurrentProperty;
		
		associationProperty = 	property.getAssociationRelatedOfFK().getOriginalAssociation();
		
		for (NodeProperty currentProperty : node.getProperties()) {				
			if(		currentProperty.isForeignKey() &&  
					!currentProperty.isPrimaryKey() && 
					!currentProperty.isMyId(property.getID())
			) {
				associationCurrent = currentProperty.getAssociationRelatedOfFK().getOriginalAssociation();
				
				//Para n�o avaliar associa��es com classes que foram achatadas
				if(! associationProperty.isMyId(associationCurrent.getID())) {
					nicknameProperty = getCommonNicknameForFK(property, node, sourceNode);
					nicknameCurrentProperty = getCommonNicknameForFK(currentProperty, node, sourceNode);
					if(nicknameProperty.equals(nicknameCurrentProperty))
						return true;
				}
			}
		}
		return false;
	}
	
	private String getNicknameColision(NodeProperty property, Node node, Node sourceNode) {
		String nickname = "";
		Node nodeEnd;
		int pos;
		
		nickname = getCommonNicknameForFK(property, node, sourceNode);
		pos = nickname.indexOf("Id");
		
		nickname  = nickname.substring(0, pos);
		
		nodeEnd = getNodeEndOf(property, node, sourceNode);
		
		nodeEnd = originalGraph.getNodeById(nodeEnd.getID());
		
		nickname = nickname + nodeEnd.getName() + "Id";
		
		return nickname;
	}
	
	private String getCommonNicknameForFK(NodeProperty property, Node node, Node sourceNode) {
		String nickname = "";
		Node rootNode, nodeEnd;
		
		nodeEnd = getNodeEndOf(property, node, sourceNode);
		
		if(nodeEnd == null)
			return "[IS NOT POSSIBLE IDENTIFY THE NICKNAME]";
		
		rootNode = originalGraph.getRootNodes(nodeEnd.getID()).get(0);
		
		nickname = rootNode.getName() + "Id";
		return nickname;
	}
	
//	private String formatName(String name) {
//		String text = name.substring(0, 1).toLowerCase();
//		
//		text += name.substring(1, name.length());
//		
//		return text;
//	}
	
	private void putFilters(TracedNode tracedNode, QueryBuilder queryBuilder) {
		for(Filter filter : tracedNode.getFilters()) {
			queryBuilder.addFilter(tracedNode.getNodeMapped(), filter.getFilterProperty().getName(), filter.getValue());
		}
	}
	
	private Node getNodeEndOf(NodeProperty property, Node node, Node sourceNode) {
		GraphAssociation fkAssociation, association;
		
		fkAssociation = property.getAssociationRelatedOfFK();
		
		association = fkAssociation.getOriginalAssociation();
		
		//When a table is created for a multivalued attribute, for example, there is no association in the original graph.
		if(association == null)
			association = fkAssociation;
		
		if(association.getSourceNode().getID().equals(sourceNode.getID())) {
			if(association.getTargetNode().getName().equals(node.getName())) // can not be itself
				return null;
			else return association.getTargetNode();
		}
		else {
			if(association.getSourceNode().getName().equals(node.getName()))// can not be itself
				return null;
			else return association.getSourceNode();
		}
	}

	
}
