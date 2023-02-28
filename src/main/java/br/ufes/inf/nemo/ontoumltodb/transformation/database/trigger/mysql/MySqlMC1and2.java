package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.mysql;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.Statistic;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.MissingConstraintData;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
//import br.ufes.inf.nemo.ontoumltodb.transformation.graph.GraphAssociation;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Cardinality;
import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class MySqlMC1and2 {

	private TraceTable traceTable;
	
	public MySqlMC1and2(TraceTable traceTable) {
		this.traceTable = traceTable;
	}
	
	public String getRestrictions(Node currentNode) {
		StringBuilder text = new StringBuilder();
		
		if(currentNode.existsMissingConstraint(MissingConstraint.MC1_2) ) {
			for(MissingConstraintData constraint : currentNode.getMissingConstraint(MissingConstraint.MC1_2)){
				text.append(getRestriction(constraint, currentNode));
			}
		}
		
		if(currentNode.existsMissingConstraint(MissingConstraint.MC1_2_Inverse) ) {
			for(MissingConstraintData constraint : currentNode.getMissingConstraint(MissingConstraint.MC1_2_Inverse)){
				text.append(getInverseRestriction(constraint, currentNode));
			}
		}
		
		return text.toString();
	}
	
	private String getRestriction(MissingConstraintData constraint, Node currentNode) {
		StringBuilder text = new StringBuilder();
		ArrayList<NodeProperty> properties = getProperties(constraint, currentNode);
		
		if(properties.isEmpty())
			return "";
		
		boolean first = true;
		String tab1 = Util.getSpaces("", Util.getTabSize());
		String tab2 = Util.getSpaces("", Util.getTabSize() * 2);
		String tab4 = Util.getSpaces("", Util.getTabSize() * 4);
		
		if(properties.size() < 2)
			return "";
		
		text.append(tab1);
		text.append("if( \n");
		text.append(tab2);
		text.append("select  ");
		
		for(NodeProperty property : properties) {
			if(first) {
				first = false;
				text.append("case when ");
				text.append("new.");
				text.append(property.getName());
				text.append(" is null ");
				text.append("then 0 else 1 end ");
			}
			else {
				text.append("+ \n");
				text.append(tab4);
				text.append("case when ");
				text.append("new.");
				text.append(property.getName());
				text.append(" is null ");
				text.append("then 0 else 1 end ");
			} 
		}
		text.append("\n");
		
		if(constraint.getSourceAssociation().getCardinalityBeginOf(constraint.getSourceNode())== Cardinality.C0_1) {
			text.append(tab2);
			text.append("  ) > 1 \n");
		}
		else {
			text.append(tab1);
			text.append("  ) <> 1 \n");
		}
		
		text.append(tab1);
		text.append("then \n");
		text.append(tab2);
		text.append("set msg = 'ERROR: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n"); 
		text.append(tab2);
		text.append("signal sqlstate '45000' set message_text = msg; \n");
		text.append(tab1);
		text.append("end if; \n\n");
		
		Statistic.addMC12();
		
		return text.toString();
	}
	
	private ArrayList<NodeProperty> getProperties(MissingConstraintData constraint, Node node ){
		ArrayList<NodeProperty> properties = new ArrayList<NodeProperty>();
		Node targetNode;
		NodeProperty fkProperty;
		TraceSet traceSet = traceTable.getTraceSetById(constraint.getSourceNode());
		
		for(Trace trace : traceSet.getTraces()) {
			targetNode = trace.getMainNode();
			fkProperty = node.getFKRelatedOfNodeID(targetNode.getID());
			if(fkProperty != null)
				properties.add(fkProperty);
		}
		return properties;
	}
	
	private String getInverseRestriction(MissingConstraintData constraint, Node currentNode) {
		StringBuilder text = new StringBuilder();
		ArrayList<Node> targetNodes;
		boolean first = true;
		String tab1 = Util.getSpaces("", Util.getTabSize());
		String tab2 = Util.getSpaces("", Util.getTabSize() * 2);
		String tab3 = Util.getSpaces("", Util.getTabSize() * 3);
		String tab5 = Util.getSpaces("", Util.getTabSize() * 5);
		
		NodeProperty destinationProperty;
		NodeProperty currentProperty = getProperty(constraint, currentNode);
		
		if(currentProperty == null)
			return "";
		
		text.append(tab1);
		text.append("if( new.");
		text.append(currentProperty.getName());
		text.append(" is not null ) \n");
		text.append(tab1);
		text.append("then \n");
		text.append(tab2);
		text.append("if( \n");
		text.append(tab3);
		text.append("select  ");
		
		targetNodes = getTargetNodes(constraint, currentNode);
		if(targetNodes.isEmpty())
			return "";
		
		for(Node targetNode : targetNodes) {
			
			if(first) {
				first = false;
			}
			else {
				text.append(" + \n");
				text.append(tab5);
			}
			
			destinationProperty = getProperty(constraint, targetNode);
			 
			text.append("case when exists( select 1 from ");
			text.append(targetNode.getName());
			text.append(" where ");
			text.append(destinationProperty.getName());
			text.append(" = ");
			text.append("new.");
			text.append(currentProperty.getName());
			text.append(") then 1 else 0 end ");
		}
		
		text.append("\n");
		text.append(tab2);
		text.append(") <> 0 \n");
		text.append(tab2);
		text.append("then \n");
		text.append(tab3);
		text.append("set msg = 'ERROR: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n"); 
		text.append(tab3);
		text.append("signal sqlstate '45000' set message_text = msg; \n");
		text.append(tab2);
		text.append("end if; \n\n");
		text.append(tab1);
		text.append("end if; \n\n");
		
		Statistic.addMC12();
		
		return text.toString();
	}
	
	private NodeProperty getProperty(MissingConstraintData constraint, Node currentNode ){
		for(GraphAssociation association : currentNode.getAssociations()) {
			if(association.getOriginalAssociation().isMyId(constraint.getSourceAssociation().getID())) {
				for(NodeProperty fk : currentNode.getProperties()) {
					if(fk.isForeignKey()) {
						if(fk.getAssociationRelatedOfFK().isMyId(association.getID())) {
							return fk;
						}
					}
				}
			}
		}
		return null;
	}
	
	private ArrayList<Node> getTargetNodes(MissingConstraintData constraint, Node currentNode){
		ArrayList<Node> result = new ArrayList<Node>();
		
		TraceSet traceSet = traceTable.getTraceSetById(constraint.getSourceNode());
		
		for(Trace trace : traceSet.getTraces()) {
			if(!trace.getMainNode().isMyId(currentNode.getID()))
				result.add(trace.getMainNode());
		}
		return result;
	}
}
