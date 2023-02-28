package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.mysql;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class MySqlTrigger {
	
	private Graph graph;
	private int tabSpaces = 4;
	
	private MySqlMC1and2 mySqlMC1and2;
	private MySqlMC3and4 mySqlMC3and4;
	private MySqlMC6 mySqlMC6;
	
	public MySqlTrigger(Graph graph, TraceTable traceTable) {
		this.graph = graph;
		
		this.mySqlMC1and2 = new MySqlMC1and2(traceTable);
		this.mySqlMC3and4 = new MySqlMC3and4(traceTable);
		this.mySqlMC6 = new MySqlMC6(traceTable);
	}

	public ArrayList<TriggerResult> getTriggers() {
		ArrayList <TriggerResult>triggers = new ArrayList<TriggerResult>();
		StringBuilder text;
		String scriptInsert = "";
		String scriptUpdate = "";
		String triggerName;
		TriggerResult result;
		
		for(Node node : graph.getNodes()) {
			text = new StringBuilder();
			
			text.append(mySqlMC1and2.getRestrictions(node));
			
			text.append(mySqlMC3and4.getRestrictions(node));
			
			text.append(mySqlMC6.getRestrictions(node));
			
			if(text.length() > 1) {
				triggerName = getTriggerNameForInsert(node);
				scriptInsert = getTriggerDeclarationInsert(node) + text.toString() + getTriggerFinalization();
				scriptInsert = scriptInsert.replace("XX_TRIGGER_NAME_XX", triggerName);
				result = new TriggerResult();
				result.setName(triggerName);
				result.setScript(scriptInsert);
				triggers.add(result);
				
				
				triggerName = getTriggerNameForUpdate(node);
				scriptUpdate = getTriggerDeclarationUpdate(node) + text.toString() + getTriggerFinalization();
				scriptUpdate = scriptUpdate.replace("XX_TRIGGER_NAME_XX", triggerName);
				result = new TriggerResult();
				result.setName(triggerName);
				result.setScript(scriptUpdate);
				triggers.add(result);
			 }
		}
		return triggers;
	}
	
	private String getTriggerDeclarationInsert(Node node) {
		String text = "";
		text += "delimiter // \n";
		text += "CREATE TRIGGER XX_TRIGGER_NAME_XX ";
		text += " BEFORE INSERT ON ";
		text += node.getName();
		text += " \nFOR EACH ROW \nBEGIN\n\n";
		text += Util.getSpaces("", tabSpaces) + "declare msg varchar(128);\n\n";
		
		return text;
	}
	
	private String getTriggerDeclarationUpdate(Node node) {
		String text = "";
		text += "delimiter // \n";
		text += "CREATE TRIGGER XX_TRIGGER_NAME_XX ";
		text += " BEFORE UPDATE ON ";
		text += node.getName();
		text += " \nFOR EACH ROW \nBEGIN\n\n";
		text += Util.getSpaces("", tabSpaces) + "declare msg varchar(128);\n\n";
		
		return text;
	}
	
	private String getTriggerFinalization() {
		return "\nEND; // \ndelimiter ;";
	}
	
	private String getTriggerNameForInsert(Node node) {
		return "tg_" + node.getName() + "_i";
	}
	
	private String getTriggerNameForUpdate(Node node) {
		return "tg_" + node.getName() + "_u";
	}
	
}
