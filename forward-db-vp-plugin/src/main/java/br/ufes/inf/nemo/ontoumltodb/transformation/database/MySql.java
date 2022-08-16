package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.MySqlTrigger;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;

public class MySql extends Generic implements IDbms {

	public MySql() {
		super();
		this.types.put("boolean", "BOOLEAN");
		this.types.put("byte", "BINARY(8)");
	}

//	public String createTableDescription() {
//		return "CREATE TABLE IF NOT EXISTS ";
//	}

//	public String getPKDescription(NodeProperty property) {
//		if (property.isPrimaryKey()) {
//			if (property.isPrimaryKeyAutoIncrement())
//				return " PRIMARY KEY"; //" AUTO_INCREMENT PRIMARY KEY";
//			return " PRIMARY KEY";
//		}
//		return "";
//	}
	
	public String createTables(Graph graph) {
		String ddl = "";
		for (Node node : graph.getNodes()) {
			//if( node.getStereotype() != Stereotype.DATATYPE ) {
				if(node.getStereotype() == Stereotype.ENUMERATION) {
					if(node.getAssociations().size() > 0) {
						ddl += createTable(node);
					}
				}
				else {
					ddl += createTable(node);
				}
			//}
		}
		return ddl;
	}
	// *****************************************************************************************
	public String createInserts(Graph graph) {
		String ddl = "";
		for (Node node : graph.getNodes()) {
			if (node.getStereotype() == Stereotype.ENUMERATION) {
				for (NodeProperty property : node.getProperties()) {
					if (property instanceof NodePropertyEnumeration) {
						
						for (String value : ((NodePropertyEnumeration) property).getValues()) {
							
							ddl += "INSERT INTO " + node.getName() + "("+ property.getName() +")"+ 
							" VALUES('" + value + "');\n";
						}
					}
				}
			}
		}
		return ddl;
	}
	
	//*************************************************************************************
	//** Method for generate the Triggers
	//*************************************************************************************
	public ArrayList<TriggerResult> generateTriggers(Graph graph, TraceTable traceTable) {
		MySqlTrigger mySqlTrigger = new MySqlTrigger(graph, traceTable);
		return mySqlTrigger.getTriggers();
	}
}
