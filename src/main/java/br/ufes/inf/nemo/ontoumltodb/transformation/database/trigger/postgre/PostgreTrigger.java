package br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.postgre;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;

public class PostgreTrigger {
	private Graph graph;
	
	private PostgreMC1and2 mySqlMC1and2;
	private PostgreMC3and4 mySqlMC3and4;
	private PostgreMC6 mySqlMC6;
	
	public PostgreTrigger(Graph graph, TraceTable traceTable) {
		this.graph = graph;
		
		this.mySqlMC1and2 = new PostgreMC1and2(traceTable);
		this.mySqlMC3and4 = new PostgreMC3and4(traceTable);
		this.mySqlMC6 = new PostgreMC6(traceTable);
	}

	public ArrayList<TriggerResult> getTriggers() {
		ArrayList <TriggerResult>triggers = new ArrayList<TriggerResult>();
		StringBuilder text;
		String script = "";
		String triggerName;
		TriggerResult result;
		
		for(Node node : graph.getNodes()) {
			text = new StringBuilder();
			
			text.append(mySqlMC1and2.getRestrictions(node));
			
			text.append(mySqlMC3and4.getRestrictions(node));
			
			text.append(mySqlMC6.getRestrictions(node));
			
			if(text.length() > 1) {
				
				triggerName = getTriggerNameFor("INSERT", node);
				script = getScriptFor("INSERT", node, text.toString(), triggerName);
				
				result = new TriggerResult();
				result.setName(triggerName);
				result.setScript(script);
				triggers.add(result);
				
				triggerName = getTriggerNameFor("UPDATE", node);
				script = getScriptFor("UPDATE", node, text.toString(), triggerName);
				
				result = new TriggerResult();
				result.setName(triggerName);
				result.setScript(script);
				triggers.add(result);
			}
		}
		return triggers;
	}
	
	private String getScriptFor(String action,Node node, String restrictions, String triggerName) {
		StringBuilder text = new StringBuilder();
		
		text.append("CREATE OR REPLACE FUNCTION ");
		text.append(getFunctionNameFor(action, node));
		text.append("\n");
		text.append("  RETURNS TRIGGER");
		text.append("\n"); 
		text.append("  LANGUAGE PLPGSQL");
		text.append("\n");
		text.append("AS ");
		text.append("\n");
		text.append("$$ ");
		text.append("\n");
		text.append("BEGIN ");
		text.append("\n");
		text.append(restrictions);
		text.append("END; ");
		text.append("\n");
		text.append("$$; ");
		text.append("\n\n");
		
		text.append("CREATE TRIGGER ");
		text.append(triggerName);
		text.append("\n");
		text.append("AFTER ");
		text.append(action);
		text.append(" ON ");
		text.append(node.getName());
		text.append("\n");
		text.append("FOR EACH ROW ");
		text.append("\n");
		text.append("EXECUTE PROCEDURE ");
		text.append(getFunctionNameFor(action, node));
		text.append("; ");
		text.append("\n");
		
		return text.toString();
	}
	
//	private String getTriggerDeclarationUpdate(Node node) {
//		String text = "";
//		text += "CREATE TRIGGER XX_TRIGGER_NAME_XX ";
//		text += " BEFORE UPDATE ON ";
//		text += node.getName();
//		text += " \nFOR EACH ROW \nBEGIN\n\n";
//		text += Util.getSpaces("", tabSpaces) + "declare msg varchar(128);\n\n";
//		
//		return text;
//	}
	
//	private String getTriggerFinalization() {
//		return "\nEND; \n$$ ;\n\n";
//	}
	
	private String getTriggerNameFor(String action, Node node) {
		if(action.equalsIgnoreCase("INSERT"))
			return "tg_" + node.getName() + "_i";
		else return "tg_" + node.getName() + "_u";
	}
	
	private String getFunctionNameFor(String action, Node node) {
		if(action.equalsIgnoreCase("INSERT"))
			return "check_" + node.getName() + "_i";
		else return "check_" + node.getName() + "_u";
	}
	
//	private String getTriggerNameForUpdate(Node node) {
//		return "tg_" + node.getName() + "_u";
//	}
	
//	private String getFunctionNameForUpdate(Node node) {
//		return "check_" + node.getName() + "_u";
//	}

}
