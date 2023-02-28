package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.mysql;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class MySqlMC3and4 {
	
//	private TraceTable traceTable;
//	private ArrayList<Filter> filtersUsed;
//	private ArrayList<String> attributesUsed;

	public MySqlMC3and4(TraceTable traceTable) {
//		this.traceTable = traceTable;
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
		text.append("set msg = 'ERROR: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n"); 
		text.append(tab2);
		text.append("signal sqlstate '45000' set message_text = msg;\n");
		
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
//	
//	private ArrayList<NodeProperty> getDependentPropertiesOf(NodeProperty currentProperty, Node node){
//		ArrayList<NodeProperty> dependentProperties = new ArrayList<NodeProperty>();
//		
//		for(NodeProperty property : node.getProperties()) {
//			if(property.isBelongsDiscriminatoryProperty()) {
//				if(property.getDiscriminatoryProperty().getName().equals(currentProperty.getName())) {
//					dependentProperties.add(property);
//				}
//			}
//		}
//		return dependentProperties;
//	}
	
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
		
//		if(isAttributeUsed(mandatoryProperty.getName())) 
//			return "";
//			
//		attributesUsed.add(mandatoryProperty.getName());
		
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
	
	/*
	public String getRestrictions(Node node) {
		StringBuilder text = new StringBuilder();
		this.filtersUsed = new ArrayList<Filter>();
		this.attributesUsed = new ArrayList<String>();
		String mc3, mc4;
		
		mc3 = getMC3Restrictions(node);
	
		mc4 = getMC4Restrictions(node);
				
		text.append(mc3);
		text.append(mc4);
		
		return text.toString();
	}
	
	private String getMC3Restrictions(Node node) {
		StringBuilder text = new StringBuilder();
		
		for(TraceSet traceSet : traceTable.getTraces()){
			for(Trace trace : traceSet.getTraces()) {
				if(trace.existsNode(node)) {
					text.append(getPropertyRuleOfTrace(trace, node));
				}
			}
		}
		return text.toString();
	}
	
	private String getPropertyRuleOfTrace(Trace trace, Node node) {
		StringBuilder text = new StringBuilder();
		TracedNode traceNodeTrigger;
		
		
		for(int i = 0; i < trace.getTracedNodes().size(); i++) {
			traceNodeTrigger = trace.getTracedNodes().get(i);
			if(traceNodeTrigger.hasMandatoryProperties()) {
				text.append(getRestrictionClausule(traceNodeTrigger));
			}
		}
		return text.toString();
	}
	
	private String getRestrictionClausule(TracedNode traceNodeTrigger) {
		StringBuilder text = new StringBuilder();
		boolean firstProperty = true;
		boolean firstFilter = true;
		boolean firstDependenteProperty = false;
		boolean putMandatoryColumns = false;
		
		StringBuilder clausule1 = new StringBuilder();
		StringBuilder clausule2;
		String tab = Util.getSpaces("", Util.getTabSize());
		String tab2 = Util.getSpaces("", Util.getTabSize()*2);
		
		for(Filter filter : traceNodeTrigger.getFilters()) {
			if(!filterAlreadyUsed(filter) && filter.hasMandatoryProperties() ) {
				
				clausule2 = new StringBuilder();
	    		
				if(firstFilter) 
					firstFilter = false;
				else {
					text.append(tab);
					text.append(" and \n");
					clausule1.append(" or ");
				}

				clausule1.append("new.");
				clausule1.append(filter.getFilterProperty().getName());
				clausule1.append(" = ");
				clausule1.append(Util.getStringValue(filter.getValue()));

				clausule2.append("new.");
				clausule2.append(filter.getFilterProperty().getName());
				clausule2.append(" <> ");
				clausule2.append(Util.getStringValue(filter.getValue()));
				clausule2.append(" and ");
				clausule1.append(" and ( ");
				
				firstProperty = true;
				putMandatoryColumns = false;
				for(NodeProperty property : filter.getMandatoryProperties()) {
					//It is not possible to validate the filling of a column according to the 
					//value "to be inserted" in another table, that is, the validation is not 
					//performed if the discriminating column does not exist in the same table 
					//of the mandatory attributes. This append for overlapping Generalization Set.
					if(traceNodeTrigger.getNodeMapped().existsPropertyName(property.getName())) {
						putMandatoryColumns = true;
						if(firstProperty) {
							clausule1.append("new.");
							clausule1.append(property.getName());
							clausule1.append(" is null ");
							firstProperty = false;
						}
						else {
							clausule1.append(" or ");
							//clausule2 += " OR ";
							clausule1.append("new.");
							clausule1.append(property.getName());
							clausule1.append(" is null ");
						}
					}
				}
				clausule1.append(" ) ");
				
				firstDependenteProperty = true;
				clausule2.append(" (");      
				for(NodeProperty dependent : getDependentPropertiesOf(filter.getFilterProperty(), traceNodeTrigger.getNodeMapped(), filter.getValue().toString() )) {
					if(firstDependenteProperty)
						firstDependenteProperty = false;
					else {
						clausule2.append(" or ");
					}
					
					if(dependent.getDataType().equalsIgnoreCase("boolean")) {
						clausule2.append("ifnull(");
						clausule2.append("new.");
						clausule2.append( dependent.getName());
						clausule2.append(", false)");
						clausule2.append(" = true");
					}
					else {
						clausule2.append("new.");
						clausule2.append( dependent.getName());
						clausule2.append(" is not null ");
					}
				}
				clausule2.append(")");
				
				text.append(tab2);
				text.append("( ");
				text.append(clausule1);
				text.append(" ) or \n");
				text.append(tab2);
				text.append("( ");
				text.append(clausule2.toString());
				text.append(" ) \n");
				text.append(tab);
				text.append(") \n");
				
				if(putMandatoryColumns) {
					filtersUsed.add(filter);
					attributesUsed.add(filter.getFilterProperty().getName());
					Statistic.addMC34();
				}
			}
		}
		
		if(!putMandatoryColumns)
			return "";
		
		if(text.length() > 1) {
			text.insert(0, tab + "if( \n");;
			text.append(tab);
			text.append("then \n");
		}
		else return "";
		
		text.append(tab2);
		text.append("set msg = 'ERROR: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n"); 
		text.append(tab2);
		text.append("signal sqlstate '45000' set message_text = msg;\n");
		text.append(tab);
		text.append("end if; \n\n");
		
		return text.toString();
	}
	
	private boolean filterAlreadyUsed(Filter filter) {
		for(Filter filterUsed : filtersUsed) {
			if(filterUsed.isSame(filter))
				return true;
		}
		return false;
	}
	
	private ArrayList<NodeProperty> getDependentPropertiesOf(NodeProperty currentProperty, Node node, String value){
		ArrayList<NodeProperty> dependentProperties = new ArrayList<NodeProperty>();
		
		for(NodeProperty property : node.getProperties()) {
			if(property.hasMandatoryProperty()) {
				if(property.getMandatoryProperty().getName().equals(currentProperty.getName())) {
					if(property.getMandatoryValue().equalsIgnoreCase(value))
						dependentProperties.add(property);
				}
			}
		}
		return dependentProperties;
	}
	
	private String getMC4Restrictions(Node node) {
		StringBuilder text = new StringBuilder();
		ArrayList<NodeProperty> dependentProperties;
		
		if(!node.existsMissingConstraint(MissingConstraint.MC3_4)) 
			return "";
		
		for(NodeProperty mandatoryProperty : node.getProperties()) {
			dependentProperties = getDependentPropertiesOf(mandatoryProperty, node);
			if(!dependentProperties.isEmpty())
				text.append(gerMC4Restriction(mandatoryProperty, dependentProperties));
		}
		return text.toString();
	}
	
	private ArrayList<NodeProperty> getDependentPropertiesOf(NodeProperty currentProperty, Node node){
		ArrayList<NodeProperty> dependentProperties = new ArrayList<NodeProperty>();
		
		for(NodeProperty property : node.getProperties()) {
			if(property.hasMandatoryProperty()) {
				if(property.getMandatoryProperty().getName().equals(currentProperty.getName())) {
					dependentProperties.add(property);
				}
			}
		}
		return dependentProperties;
	}
	
	private String gerMC4Restriction(NodeProperty mandatoryProperty, ArrayList<NodeProperty> dependentProperties) {
		StringBuilder text = new StringBuilder();
		boolean first = true;
		String value = dependentProperties.get(0).getMandatoryValue(); // all dependent properties must have the same mandatory value.
		String tab = Util.getSpaces("", Util.getTabSize());
		String tab2 = Util.getSpaces("", Util.getTabSize()*2);
		
		if(isAttributeUsed(mandatoryProperty.getName())) 
			return "";
			
		attributesUsed.add(mandatoryProperty.getName());
		
		text.append(tab);
		text.append("if( \n");
		text.append(tab2);
		text.append("( new.");
		text.append(mandatoryProperty.getName());
		text.append(" <> ");
		
		if(isBooleanColumn(mandatoryProperty)) {
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
		text.append(tab);
		text.append(") \n");
		text.append(tab);
		text.append("then \n");
		text.append(tab2);
		text.append("set msg = 'ERROR: Violating conceptual model rules[XX_TRIGGER_NAME_XX].'; \n"); 
		text.append(tab2);
		text.append("signal sqlstate '45000' set message_text = msg;\n");
		
		text.append(tab);
		text.append("end if; \n\n");
		
		Statistic.addMC34();
		
		return text.toString();
	}
	
	private boolean isBooleanColumn(NodeProperty property) {
		
		if(property.getDataType().equalsIgnoreCase("BOOLEAN"))
			return true;
		return false;
	}
	
	private boolean isAttributeUsed(String name) {
		for(String aux : this.attributesUsed) {
			if(aux.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	*/
}
