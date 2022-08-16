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
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class MySqlMC3and4 {
	
	private TraceTable traceTable;
	private ArrayList<Filter> filtersUsed;

	public MySqlMC3and4(TraceTable traceTable) {
		this.traceTable = traceTable;
	}
	
	public String getRestrictions(Node node) {
		StringBuilder text = new StringBuilder();
		this.filtersUsed = new ArrayList<Filter>();
		
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
		String text = "";
		boolean firstProperty = true;
		boolean firstFilter = true;
		boolean putMandatoryColumns = false;
		
		String clausule1 = "";
		String clausule2 = "";
		String tab = Util.getSpaces("", Util.getTabSpaces());
		
		for(Filter filter : traceNodeTrigger.getFilters()) {
			if(!filterAlreadyUsed(filter) && filter.hasMandatoryProperties() ) {
				filtersUsed.add(filter);
	    		
				if(firstFilter) 
					firstFilter = false;
				else {
					text +=  tab +" AND \n";
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
					//of  the mandatory attributes.
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
				
				text +=  tab + tab + "( " +  clausule1 + " ) OR \n";
				text +=  tab + tab + "( " +  clausule2 + " ) ";
				text += "\n";
				text += tab + "  ) \n";
				
				if(putMandatoryColumns)
					Statistic.addMC34();

			}
		}
		
		if(!putMandatoryColumns)
			return "";
		
		if(text.length() > 1) {
			text = tab + "if( \n" + text;
			text += tab + "then \n";
		}
		else return "";
		
		text += tab + tab + "set msg = 'ERROR: Violating conceptual model rules";
		text += "[XX_TRIGGER_NAME_XX].'; \n"; 
		text += tab + tab + "signal sqlstate '45000' set message_text = msg;\n";
		
		text += tab + "end if; \n\n";
		
		return text;
	}
	
	private boolean filterAlreadyUsed(Filter filter) {
		for(Filter filterUsed : filtersUsed) {
			if(filterUsed.isSame(filter))
				return true;
		}
		return false;
	}
}
