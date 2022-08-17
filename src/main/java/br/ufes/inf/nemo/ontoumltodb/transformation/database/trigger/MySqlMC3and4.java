package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.Statistic;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Filter;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.Trace;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TracedNode;
import br.ufes.inf.nemo.ontoumltodb.util.MissingConstraint;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class MySqlMC3and4 {
	
	private TraceTable traceTable;
	private ArrayList<Filter> filtersUsed;
	private ArrayList<String> attributesUsed;

	public MySqlMC3and4(TraceTable traceTable) {
		this.traceTable = traceTable;
	}
	
	public String getRestrictions(Node node) {
		StringBuilder text = new StringBuilder();
		this.filtersUsed = new ArrayList<Filter>();
		this.attributesUsed = new ArrayList<String>();
		String mc3, mc4;
		
		
		//Next version: change MC3 constraint generation from trace table to node (as done for MC4).
		mc3 = getMC3Restrictions(node);
		mc4 = getMC4Restrictions(node);
				
		text.append(mc3);
		text.append(mc4);
		
		
//		for(TraceSet traceSet : traceTable.getTraces()){
//			for(Trace trace : traceSet.getTraces()) {
//				if(trace.existsNode(node)) {
//					text.append(getPropertyRuleOfTrace(trace, node));
//				}
//			}
//		}
		
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
		String text = "";
		TracedNode traceNodeTrigger;
		
		
		for(int i = 0; i < trace.getTracedNodes().size(); i++) {
			traceNodeTrigger = trace.getTracedNodes().get(i);
			if(traceNodeTrigger.hasMandatoryProperties()) {
				text += getRestrictionClausule(traceNodeTrigger);
			}
		}
		return text;
	}
	
	private String getRestrictionClausule(TracedNode traceNodeTrigger) {
		StringBuilder text = new StringBuilder();
		boolean firstProperty = true;
		boolean firstFilter = true;
		boolean putMandatoryColumns = false;
		
		String clausule1 = "";
		String clausule2 = "";
		String tab = Util.getSpaces("", Util.getTabSpaces());
		String tab2 = Util.getSpaces("", Util.getTabSpaces()*2);
		
		for(Filter filter : traceNodeTrigger.getFilters()) {
			if(!filterAlreadyUsed(filter) && filter.hasMandatoryProperties() ) {
				filtersUsed.add(filter);
				attributesUsed.add(filter.getFilterProperty().getName());
	    		
				if(firstFilter) 
					firstFilter = false;
				else {
					text.append(tab);
					text.append(" AND \n");
					clausule1 += " OR ";
					clausule2 += " OR ";
				}

				clausule1 = " NEW." + filter.getFilterProperty().getName()+ " = " + Util.getStringValue(filter.getValue());
				clausule2 = " NEW." + filter.getFilterProperty().getName() + " <> " + Util.getStringValue(filter.getValue());
				clausule1 += " AND ( ";
				clausule2 += " AND ( ";
				
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
							clausule1 += "NEW." + property.getName() + " is null ";
							clausule2 += "NEW." + property.getName() + " is not null ";
							firstProperty = false;
						}
						else {
							clausule1 += " OR ";
							clausule2 += " OR ";
							clausule1 += "NEW." + property.getName() + " is null ";
							clausule2 += "NEW." + property.getName() + " is not null ";
						}
					}
				}
				
				clausule1 += " ) ";
				clausule2 += " ) ";
				
				text.append(tab2);
				text.append("( ");
				text.append(clausule1);
				text.append(" ) OR \n");
				text.append(tab2);
				text.append("( ");
				text.append(clausule2);
				text.append(" ) \n");
				text.append(tab);
				text.append("  ) \n");
				
				if(putMandatoryColumns)
					Statistic.addMC34();
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
		String tab = Util.getSpaces("", Util.getTabSpaces());
		String tab2 = Util.getSpaces("", Util.getTabSpaces()*2);
		
		
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
		
		text.append(" AND ");
		text.append(" ( ");
		
		for(NodeProperty property : dependentProperties) {
			if(first)
				first = false;
			else text.append(" OR ");
			
			if(isBooleanColumn(property)) {
				text.append(" (");
				text.append("new.");
				text.append(property.getName());
				text.append(" is not null ");
				
				text.append(" and ");
				
				text.append(" new.");
				text.append(property.getName());
				text.append(" = true");
				
				text.append(") ");
					
//					text.append(" ( ifnull(");
//					text.append("new.");
//					text.append(property.getName());
//					text.append(", false)");
//					
//					text.append(" = true");
//					
//					text.append(") ");
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
}
