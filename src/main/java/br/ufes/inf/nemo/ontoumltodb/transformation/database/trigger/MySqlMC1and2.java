package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.Statistic;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.ConstraintData;
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
//		ArrayList <NodeProperty> duplicateProperties;
//		ArrayList <NodeProperty> duplicatePropertiesConstraint;
		
		
		if(currentNode.existsMissingConstraint(MissingConstraint.MC1_2) ) {
			for(ConstraintData constraint : currentNode.getMissingConstraint(MissingConstraint.MC1_2)){
				text.append(getRestriction(constraint, currentNode));
			}
			
//			duplicateProperties = getFksForTheSameOriginalClass(node);
//			
//			while( ! duplicateProperties.isEmpty()) {
//				//The node can have more the one association with flattened nodes.
//				duplicatePropertiesConstraint = extractFirstDuplicateKey(duplicateProperties);
//				text.append(getConstraintLostInFlattening(duplicatePropertiesConstraint));
//			}
		}
		
		if(currentNode.existsMissingConstraint(MissingConstraint.MC1_2_Inverse) ) {
			for(ConstraintData constraint : currentNode.getMissingConstraint(MissingConstraint.MC1_2_Inverse)){
				text.append(getInverseRestriction(constraint, currentNode));
			}
		}
		
		return text.toString();
	}
	
	private String getRestriction(ConstraintData constraint, Node currentNode) {
		StringBuilder text = new StringBuilder();
		ArrayList<NodeProperty> properties = getProperties(constraint, currentNode);
		
		if(properties.isEmpty())
			return "";
		
		boolean first = true;
		String tab1 = Util.getSpaces("", Util.getTabSpaces());
		String tab2 = Util.getSpaces("", Util.getTabSpaces() * 2);
		String tab4 = Util.getSpaces("", Util.getTabSpaces() * 4);
		
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
	
	private ArrayList<NodeProperty> getProperties(ConstraintData constraint, Node node ){
		ArrayList<NodeProperty> properties = new ArrayList<NodeProperty>();
		Node targetNode;
		NodeProperty fkProperty;
		TraceSet traceSet = traceTable.getTraceSetOfById(constraint.getSourceNode());
		
		for(Trace trace : traceSet.getTraces()) {
			targetNode = trace.getMainNode();
			fkProperty = node.getFKRelatedOfNodeID(targetNode.getID());
			if(fkProperty != null)
				properties.add(fkProperty);
		}
		return properties;
	}
	
	private String getInverseRestriction(ConstraintData constraint, Node currentNode) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		String tab1 = Util.getSpaces("", Util.getTabSpaces());
		String tab2 = Util.getSpaces("", Util.getTabSpaces() * 2);
		String tab3 = Util.getSpaces("", Util.getTabSpaces() * 3);
		String tab5 = Util.getSpaces("", Util.getTabSpaces() * 5);
		
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
		
		for(Node targetNode : getTargetNodes(constraint, currentNode)) {
			
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
	
	private NodeProperty getProperty(ConstraintData constraint, Node currentNode ){
		//Node sourceNodeEnd = constraint.getSourceAssociation().getNodeEndOf(constraint.getSourceNode());
		//Node targetNodeEnd = traceTable.getTraceSetOfById(sourceNodeEnd).getTraces().get(0).getMainNode();
		//NodeProperty property  = currentNode.getFKRelatedOfNodeID(targetNodeEnd.getID());		
		
		for(GraphAssociation association : currentNode.getAssociations()) {
			if(association.getOriginalAssociation().isMyId(constraint.getSourceAssociation().getID())) {
				for(NodeProperty fk : currentNode.getProperties()) {
					if(fk.isForeignKey()) {
						if(fk.getAssociationRelatedOfFK().isMyId(association.getID())) {
							return fk;
//							if(currentNode.getName().equals("method_member_function") ) {
//								System.out.println("*******************");
//								System.out.println("Current Node: " + currentNode.toString());
//								System.out.println("Constraint sourceNode: " + constraint.getSourceNode().toString());
//								System.out.println("constraint association: " + constraint.getSourceAssociation().toString() );
//								System.out.println("sourceNodeEnd: " + sourceNodeEnd.toString());
//								System.out.println("targetNodeEnd: " + targetNodeEnd.toString());
//							}
						}
					}
				}
			}
		}
		return null;
	}
	
	private ArrayList<Node> getTargetNodes(ConstraintData constraint, Node currentNode){
		ArrayList<Node> result = new ArrayList<Node>();
		
		TraceSet traceSet = traceTable.getTraceSetOfById(constraint.getSourceNode());
		
		for(Trace trace : traceSet.getTraces()) {
			if(!trace.getMainNode().isMyId(currentNode.getID()))
				result.add(trace.getMainNode());
		}
		return result;
	}
}
