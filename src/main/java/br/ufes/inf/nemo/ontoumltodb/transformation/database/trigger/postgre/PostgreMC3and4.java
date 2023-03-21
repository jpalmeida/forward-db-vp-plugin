package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.postgre;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class PostgreMC3and4 {

	public PostgreMC3and4(TraceTable traceTable) {
	}
	
	public String getRestrictions(Node node) {
		StringBuilder text = new StringBuilder();
		ArrayList<NodeProperty> slaveProperties;
		ArrayList<NodeProperty> dependentProperties;
		NodeProperty discriminatoryProperty, slave;
		String discriminatoryValue;
		
		if(!node.existsMissingConstraint(MissingConstraint.MC3_4)) 
			return "";
		
		slaveProperties = getSlaveProperties(node);
		
		while(!slaveProperties.isEmpty()) {
			slave = slaveProperties.get(0);
			discriminatoryProperty = slave.getDiscriminatoryProperty();
			discriminatoryValue = slave.getDiscriminatoryValue();
			
			dependentProperties = getDependentProperties(discriminatoryProperty, discriminatoryValue, slaveProperties);
			
			text.append(getMC3and4Restrictions(discriminatoryProperty, discriminatoryValue, dependentProperties));
			
		}
		
		return text.toString();
	}
	
	private ArrayList<NodeProperty> getSlaveProperties(Node node){
		ArrayList<NodeProperty> slaveProperties = new ArrayList<NodeProperty>();
		for(NodeProperty property : node.getProperties()) {
			if(property.isBelongsDiscriminatoryProperty()) {
				slaveProperties.add(property);
			}
		}
		return slaveProperties;
	}
	
	private ArrayList<NodeProperty> getDependentProperties(NodeProperty discriminitor, String value, ArrayList<NodeProperty> slaveProperties){
		ArrayList<NodeProperty> dependentProperties = new ArrayList<NodeProperty>();
		NodeProperty property;
		int index = 0;
		
		while(index < slaveProperties.size()) {
			property = slaveProperties.get(index);
			
			if(	property.getDiscriminatoryProperty().getName().equals(discriminitor.getName()) &&
				property.getDiscriminatoryValue().equals(value))	{
				dependentProperties.add(property);
				slaveProperties.remove(index);
			}
			else {
				index++;
			}
		}
		return dependentProperties;
	}
	
	private String getMC3and4Restrictions(NodeProperty discriminitor, String value, ArrayList<NodeProperty> dependentProperties) {
		StringBuilder text = new StringBuilder();
		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
		String tab = Util.getSpaces("", Util.getTabSize());
		String tab2 = Util.getSpaces("", Util.getTabSize()*2);
		
		mandatoryProperties = getMandatoryProperties(dependentProperties);
		
		text.append(tab);
		text.append("if( \n");
		
		if(!mandatoryProperties.isEmpty()) {
			text.append(gerMC3Restriction(discriminitor, value, mandatoryProperties));
			text.append(" or \n");
		}
		
		if(!dependentProperties.isEmpty())
			text.append(gerMC4Restriction(discriminitor, value, dependentProperties));
		
		text.append(tab);
		text.append(") \n");
		text.append(tab);
		text.append("then \n");
		text.append(tab2);
		text.append("RAISE EXCEPTION  'ERROR 3: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n"); 

		text.append(tab);
		text.append("end if; \n\n");
		
		return text.toString();
	}
	
	private ArrayList<NodeProperty> getMandatoryProperties(ArrayList<NodeProperty> dependentProperties){
		ArrayList<NodeProperty> mandatoryProperties = new ArrayList<NodeProperty>();
		
		for(NodeProperty property :dependentProperties) {
			if(property.isMandatoryFillingWhenMandatoryPropertyIsFilled()) {
				mandatoryProperties.add(property);
			}
		}
		return mandatoryProperties;
	}
	
	private String gerMC3Restriction(NodeProperty discriminatorProperty, String value, ArrayList<NodeProperty> mandatoryProperties) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		String tab2 = Util.getSpaces("", Util.getTabSize()*2);
		
		text.append(tab2);
		text.append("( new.");
		text.append(discriminatorProperty.getName());
		text.append(" = ");
		
		if(isBooleanColumn(discriminatorProperty)) {
			text.append(value.toUpperCase());
		}
		else {
			text.append("'");
			text.append(value.toUpperCase());
			text.append("'");	
		}
		
		text.append(" and ");
		text.append(" ( ");
		
		for(NodeProperty property : mandatoryProperties) {
			if(first)
				first = false;
			else text.append(" or ");
			
			text.append("new.");
			text.append(property.getName());
			text.append(" is null");	
		}
		
		text.append(" ) ");
		text.append(" ) ");
		
		return text.toString();
	}
	
	private String gerMC4Restriction(NodeProperty discriminatorProperty, String value, ArrayList<NodeProperty> dependentProperties) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		String tab2 = Util.getSpaces("", Util.getTabSize()*2);
		
		text.append(tab2);
		text.append("( new.");
		text.append(discriminatorProperty.getName());
		text.append(" <> ");
		
		if(isBooleanColumn(discriminatorProperty)) {
			text.append(value.toUpperCase());
		}
		else {
			text.append("'");
			text.append(value.toUpperCase());
			text.append("'");	
		}
		
		text.append(" and ");
		text.append(" ( ");
		
		for(NodeProperty property : dependentProperties) {
			if(first)
				first = false;
			else text.append(" or ");
			
			if(isBooleanColumn(property)) {
				text.append(" ifnull(");
				text.append("new.");
				text.append(property.getName());
				text.append(", false)");
				
				text.append(" = true");
			}
			else {
				text.append("new.");
				text.append(property.getName());
				text.append(" is not null");
			}			
		}
		
		text.append(" ) ");
		text.append(" ) \n");
		
		return text.toString();
	}
	
	private boolean isBooleanColumn(NodeProperty property) {
		
		if(property.getDataType().equalsIgnoreCase("BOOLEAN"))
			return true;
		return false;
	}
}
