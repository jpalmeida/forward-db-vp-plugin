package br.ufes.inf.nemo.ontoumltodb.transformation.database.view;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.SqlUtil;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.NodeOrigin;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class QueryBuilder {

	private ArrayList<QueryColumn> columns;
	private ArrayList<Node> tables;
	private ArrayList<QueryFilter> filters;
	
	public QueryBuilder() {
		this.columns = new ArrayList<QueryColumn>();
		this.tables = new ArrayList<Node>();
		this.filters = new ArrayList<QueryFilter>();
	}
	
	public void addColumn(Node table, String columnName, String nickname) {
		columns.add(new QueryColumn(table.getName(), columnName, nickname));
		
		if(!existsTable(table)) {
			tables.add(table);
		}
	}
	
	public void addTable(Node table) {
		if(!existsTable(table)) {
			tables.add(table);
		}
	}
	
	public void addFilter(Node table, String columnName, Object value) {
		if(! existsFilter(table, columnName)) {
			filters.add(new QueryFilter(table.getName(), columnName, value));
		}
		
		if(!existsTable(table)) {
			tables.add(table);
		}
	}
	
	private boolean existsTable(Node table) {
		for(Node nodeTable : tables) {
			if(nodeTable.isMyId(table.getID()))
				return true;
		}
		return false;
	}
	
	private boolean existsFilter(Node table, String columnName) {
		for(QueryFilter myFilter : filters) {
			if(myFilter.getTableName().equals(table.getName()) && myFilter.getColumnName().equals(columnName))
				return true;
		}
		return false;
	}
	
	public boolean existsColumn(String tableName, String columnName, String nickname) {
		for(QueryColumn column : columns) {
			if(column.existsColumn(tableName, columnName, nickname))
				return true;
		}
		return false;
	}
	
	public boolean existsColumnName(String columnName) {
		for(QueryColumn column : columns) {
			if(column.existsColumnName(columnName))
				return true;
		}
		return false;
	}
	
	public boolean existsNickname(String nickname) {
		for(QueryColumn column : columns) {
			if(column.existsNickname(nickname))
				return true;
		}
		return false;
	}
	
	public String getQuery() {
		String text = "";
		
		text += getSelect();
		text += getFrom();
		text += getWhere();
		
		return text;
	}
	//********************************************************************************
	//** S E L E C T 
	//********************************************************************************
	private String getSelect() {
		ArrayList<QueryColumn> currentColumns;
		ArrayList<QueryColumn> buildColumns;
		
		boolean first = true;
		String text = "";
		
		currentColumns = getCurrentColumns();
		
		while(currentColumns.size() > 0) {
			buildColumns = getColumnsToBuild(currentColumns);
			
			if(first) {
				text = "SELECT";
				text += Util.getSpaces("SELECT", 7);
				first = false;
			}
			else {
				text += "\n";
				text += ",";
				text += Util.getSpaces(",", 7);
			}
			if(buildColumns.size() == 1) {
				text += getTextForSingleColumn(buildColumns.get(0));
			}
			else {
				text += getTextForDuplicatedColumns(buildColumns);
			}
		}
		if(text.length() > 1)
			text += "\n";
		return text;
	}
	
	private ArrayList<QueryColumn> getCurrentColumns(){
		ArrayList<QueryColumn> result = new ArrayList<QueryColumn>();
		for(QueryColumn column : columns) {
			result.add(column);
		}
		return result;
	}
	
	private ArrayList<QueryColumn> getColumnsToBuild(ArrayList<QueryColumn> currentColumns){
		ArrayList<QueryColumn> result = new ArrayList<QueryColumn>();
		QueryColumn column = currentColumns.remove(0);
		QueryColumn avaliatedColumn;
		result.add(column);
		int index = 0;
		
		while(index < currentColumns.size()) {
			avaliatedColumn = currentColumns.get(index);
			if(column.getNickname().equals(avaliatedColumn.getNickname())) {
				result.add(avaliatedColumn);
				currentColumns.remove(index);
			}
			else {
				index++;
			}
		}
		return result;
	}
	
	private String getTextForSingleColumn(QueryColumn column) {
		String text = "";
		
		text += column.getTableName();
		text += ".";
		text += column.getColumnName();
		text += " AS ";
		text += column.getNickname();
		
		return text;
	}
	
	private String getTextForDuplicatedColumns( ArrayList<QueryColumn> duplicateds){
		String text = "";
		String spaces = "        ";
		String textEnd = "";
		QueryColumn column = duplicateds.get(0);
		
		text += "IFNULL(" + column.getTableName() + "." + column.getColumnName();
		textEnd += ") ";
		
		for(int i = 1; i < duplicateds.size(); i++) {
			column = duplicateds.get(i);
			spaces += "       ";
			text += ",\n" + spaces;
			text += " IFNULL(" + column.getTableName() + "." + column.getColumnName();
			textEnd += "\n" + spaces + ") ";	
		}
		text += ", NULL";
		text += textEnd;
		
		text += " AS ";
		text += column.getNickname();
		return text;
	}
	
	//********************************************************************************
	//** F R O M
	//********************************************************************************
	private String getFrom() {
		String text = "";
		String joinClausule = "";
		String filterClausule = "";
		ArrayList<GraphAssociation> allAssociations;
		ArrayList<GraphAssociation> currentAssociations;
		Node destinationNode;
		
		text += "FROM";
		text += Util.getSpaces("FROM", 7);
		text += tables.get(0).getName();
		
		allAssociations = getAssociations();
		
		for(Node table : tables) {
			
			currentAssociations = removeAssociationsOfTable(table, allAssociations);
			
			for(GraphAssociation association : currentAssociations) {
				joinClausule = "";
				filterClausule = "";
				
				destinationNode = getDestinationTable(table, association);
					
				joinClausule = getJoinForTheAssociation(table, association);
				filterClausule = getFilter(destinationNode);
					
				text += joinClausule + filterClausule;
			}
		}
		if(text.length() > 1)
			text += "\n";
		return text;
	}
	
	private ArrayList<GraphAssociation> getAssociations() {
		ArrayList<GraphAssociation> associations = new ArrayList<GraphAssociation>();
		
		for (Node table : tables) {
			for(GraphAssociation association : table.getAssociations()) {
				if(hasAssociationBetweenNodes(association)) {
					if(!existsAssociation(associations, association))
						associations.add(association);
				}
			}
		}
		return associations;
	}
	
	private boolean hasAssociationBetweenNodes(GraphAssociation association) {
		if(existsTable(association.getSourceNode()) && existsTable(association.getTargetNode()))
			return true;
		else return false;
	}
	
	private static boolean existsAssociation(ArrayList<GraphAssociation> associations, GraphAssociation associationAvaliable) {
		for(GraphAssociation association : associations) {
			if(association.isMyId(associationAvaliable.getID()))
				return true;
		}
		return false;
	}
	
	private ArrayList<GraphAssociation> removeAssociationsOfTable(Node node, ArrayList<GraphAssociation> allAssociations) {
		ArrayList<GraphAssociation> result = new ArrayList<GraphAssociation>();
		GraphAssociation association;
		int i = 0;
		
		while(i < allAssociations.size()) {
			association = allAssociations.get(i);
			
			if	(association.getSourceNode().getID().equals(node.getID()) ||
				 association.getTargetNode().getID().equals(node.getID())) {
				result.add(association);
				allAssociations.remove(i);
			}
			else {
				i++;
			}	
		}
		return result;
	}
	
	private Node getDestinationTable(Node fromTable, GraphAssociation association) {
		if(association.getSourceNode().getID().equals(fromTable.getID())) {
			return association.getTargetNode();
			
		}else {
			return association.getSourceNode();
		}
	}
	
	private String getJoinForTheAssociation(Node table, GraphAssociation association) {
		Node destinationTable;
		
		destinationTable = getDestinationTable(table, association);
		
		if(destinationTable.existsPropertyName(table.getPKName())) {
			return getJoinClausule(table, destinationTable, association);
		}
		else {
			if(table.existsPropertyName(destinationTable.getPKName())) {
				return getJoinClausule(destinationTable, table, association);
			}
		}
		return "[NOT WAS POSSIBLE ESTABILISH THE JOIN (getJoinForTheAssociation)]";
	}
	
	private String getJoinClausule(Node sourceTable, Node targetTable, GraphAssociation association) {
		String text = "";
		Cardinality cardinalityEnd;
		
		cardinalityEnd = getCardinalityEnd(sourceTable, association);
		
		NodeProperty fk = targetTable.getFKRelatedOfNodeID(sourceTable.getID());
		
		text = getJoin(targetTable, cardinalityEnd, association);
		text += Util.getSpaces(text, 12);
		text += targetTable.getName();
		text += "\n";
		text += Util.getSpaces("", 8);
		text += "ON";
		text += Util.getSpaces("ON", 4);
		text += sourceTable.getName();
		text += ".";
		text += sourceTable.getPKName();
		text += " = ";
		text += targetTable.getName();
		text += ".";
		text += fk.getName();
		
		return text;
	}
	
	private String getJoin(Node destinationNode, Cardinality cardinalityEnd, GraphAssociation association) {
		String text = "\n";
		
		if(		destinationNode.getNodeOrigin() == NodeOrigin.FROM_GENERALIZATION_SET ||
				destinationNode.getNodeOrigin() == NodeOrigin.FROM_N_TO_N ||
				association.isDerivedFromGeneralization()) {
			text += "INNER JOIN";
		}
		else {
			if(cardinalityEnd == Cardinality.C0_1 || cardinalityEnd == Cardinality.C0_N) {
				text += "LEFT JOIN";
			}
			else {
				text += "INNER JOIN";
			}
		}
		return text;
	}
	
	private Cardinality getCardinalityEnd(Node originNode, GraphAssociation association) {
		if(association.getSourceNode().getID().equals(originNode.getID())) 
			return association.getTargetCardinality();
		else return association.getSourceCardinality();
		
	}
	
	private String getFilter(Node table) {
		String text = "";
		
		for(QueryFilter filter : filters) {
			if(filter.getTableName().equals(table.getName())) {
				text += "\n";
				text += Util.getSpaces("", 8);
				text += "AND";
				text += Util.getSpaces("AND", 4);
				text += filter.getTableName();
				text += ".";
				text += filter.getColumnName();
				text += " = ";
				text += Util.getStringValue(filter.getValue());
				text += " ";
				filter.setApplied(true);
			}
		}
		return text;
	}


	//********************************************************************************
	//** W H E R E
	//********************************************************************************
	private String getWhere() {
		boolean first = true;
		String text = "";

		for (QueryFilter filter : filters) {

			if(!filter.isApplied()) {
				if (first) {
					text += "WHERE";
					text +=  Util.getSpaces("WHERE", 7);
					first = false;
				} else {
					text += "\n";
					text += "AND";
					text +=  Util.getSpaces("AND", 7);
				}
	
				text += filter.getTableName();
				text += ".";
				text += filter.getColumnName();
				text += " = ";
				text += SqlUtil.getStringValue(filter.getValue());
			}
		}
		
		if(text.length() > 1)
			text += "\n";
		return text;
	}
}
