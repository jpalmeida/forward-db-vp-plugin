package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.Statistic;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.ConstraintData;
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
		
		/*
    if (
    	select 
            case when exists (select 1 from variable where name_name_id = new.name_id) then 1 else 0 end  + 
            case when exists (select 1 from method_member_function where name_name_id = new.name_id) then 1 else 0 end 
    ) <> 0 
    then 
        set msg = 'ERROR: Violating conceptual model rules [tg_class_i].'; 
        signal sqlstate '45000' set message_text = msg; 
    end if; 
		*/
	}
	
	private NodeProperty getProperty(ConstraintData constraint, Node currentNode ){
		Node sourceNodeEnd = constraint.getSourceAssociation().getNodeEndOf(constraint.getSourceNode());
		Node targetNodeEnd = traceTable.getTraceSetOfById(sourceNodeEnd).getTraces().get(0).getMainNode();
		NodeProperty property  = currentNode.getFKRelatedOfNodeID(targetNodeEnd.getID());
		return property;
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
	
	
	/*
	
	
	private ArrayList <NodeProperty> getFksForTheSameOriginalClass(Node node) {
		Node originalNode;
		NodeProperty property1, property2;
		GraphAssociation association1, association2;
		ArrayList <NodeProperty> duplicateProperties = new ArrayList<NodeProperty>();
		
		originalNode = traceTable.getOriginalGraph().getNodeById(node.getID());
		
		//If not find, is because the node was created in the process. There is nothing to validate.
		if(originalNode == null) {
			return duplicateProperties;
		}
		
		for(int index_1 = 0; index_1 < node.getProperties().size(); index_1++) {
			property1 = node.getProperties().get(index_1);
			if(property1.isForeignKey() && !property1.isSelfAssociation()) {
				for(int index_2 = index_1; index_2 < node.getProperties().size(); index_2++) {
					property2 = node.getProperties().get(index_2);
					
					if( (! property1.isMyId(property2.getID())) && 
							property2.isForeignKey() 
					) {
						association1 = property1.getAssociationRelatedOfFK().getOriginalAssociation();
						association2 = property2.getAssociationRelatedOfFK().getOriginalAssociation();
						
						if(association1 != null && association2 != null) {
							if(hasTheSameClassAssociated(association1, association2, originalNode)) {
								addIfNotExists(duplicateProperties, property1);
								addIfNotExists(duplicateProperties, property2);
							}
						}	
					}
				}
			}
		}
		return duplicateProperties;
	}
	
	private ArrayList<NodeProperty> extractFirstDuplicateKey(ArrayList<NodeProperty> duplicateProperties){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		NodeProperty property;
		int index = 0;
		
		property = duplicateProperties.remove(0);
		
		result.add(property);
		
		while( (index = getNext(property, duplicateProperties))!= -1) {
			result.add(duplicateProperties.remove(index));			
		}
		
		return result;
	}
	
	private String getConstraintLostInFlattening(ArrayList<NodeProperty> properties) {
		String text = "";
		boolean first = true;
		String tab = Util.getSpaces("", Util.getTabSpaces());
		String largeTab = Util.getSpaces("", Util.getTabSpaces() * 4);
		
		text += tab + "if( \n";
		text += tab + tab + "SELECT  ";
		for(NodeProperty property : properties) {
			if(first) {
				first = false;
				text += "case when ";
				text += "NEW."+ property.getName() + " is null ";
				text += "then 0 else 1 end ";
			}
			else {
				text += "+ \n";
				text += largeTab + "case when ";
				text += "NEW."+ property.getName() + " is null ";
				text += "then 0 else 1 end ";
			} 
		}
		text += "\n";
		
		NodeProperty property1 = properties.get(0);
		GraphAssociation association1 = property1.getAssociationRelatedOfFK().getOriginalAssociation();
		if(association1.getCardinalityEndOf(property1.getOwnerNode()) == Cardinality.C0_1) {
			text += tab + "  ) > 1 \n";
		}
		else text += tab + "  ) <> 1 \n";
		
		text += tab + "then \n";
		
		tab = Util.getSpaces("", Util.getTabSpaces() * 3);
		
		text += tab + "set msg = 'ERROR: Violating conceptual model rules";
		text += "[XX_TRIGGER_NAME_XX].'; \n"; 
		text += tab + "signal sqlstate '45000' set message_text = msg; \n";
		
		tab = Util.getSpaces("", Util.getTabSpaces() );
		text += tab + "end if; \n\n";
		
		Statistic.addMC12();
		
		return text;
	}
	
	private boolean hasTheSameClassAssociated(GraphAssociation association1, GraphAssociation association2, Node originalNode) {
		Node nodeEnd1, nodeEnd2;
		
		if(association1.getSourceNode().isMyId(originalNode.getID()))
			nodeEnd1 = association1.getTargetNode();
		else nodeEnd1 = association1.getSourceNode();
		
		if(association2.getSourceNode().isMyId(originalNode.getID()))
			nodeEnd2 = association2.getTargetNode();
		else nodeEnd2 = association2.getSourceNode();
		
		if(nodeEnd1.isMyId(nodeEnd2.getID()))
			return true;
		else return false;
		
	}
	
	private void addIfNotExists(ArrayList <NodeProperty> duplicateProperties, NodeProperty property) {
		boolean find = false;
		
		for(NodeProperty currentProperty : duplicateProperties) {
			if(currentProperty.isMyId(property.getID()))
				find = true;
		}
		
		if( ! find)
			duplicateProperties.add(property);
	}
	
	private int getNext(NodeProperty property, ArrayList<NodeProperty> duplicateProperties) {
		NodeProperty currentProperty;
		for(int i = 0; i < duplicateProperties.size(); i++) {
			currentProperty = duplicateProperties.get(i);
			if(property.getAssociationRelatedOfFK().getOriginalAssociation().isMyId(currentProperty.getAssociationRelatedOfFK().getOriginalAssociation().getID())) {
				return i;
			}
		}
		return -1;
	}
	*/
}
