package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import java.util.ArrayList;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.postgre.PostgreTrigger;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class Postgre extends Generic implements IDbms {

	public Postgre() {
		super();
		this.types.put("boolean", "BOOLEAN");
		this.types.put("byte", "BYTE(4)");
		this.types.put("double", "FLOAT(8)");
		this.types.put("float", "FLOAT(4)");
		this.types.put("int", "INTEGER");
	}

	public String getSchema(Graph graph) {
		String ddl = ""; 
		
		ddl += generateEnums(graph);
		
		ddl += super.getSchema(graph);

		return ddl;
	}
//	public String createTableDescription() {
//		return "CREATE TABLE IF NOT EXISTS ";
//	}

	public String getPKDescription(NodeProperty property) {
		if (property.isPrimaryKey())
			return " PRIMARY KEY";
		else
			return "";
	}

	public String getColumnType(NodeProperty property) {
		String ddl = "";

		if (property instanceof NodePropertyEnumeration) {
			ddl = property.getName() + "_enum_type";
		} else {
			if (property.isPrimaryKey()) {
				ddl += "SERIAL ";
			} else {
				if (this.types.get(property.getDataType()) != null) {
					ddl = this.types.get(property.getDataType());
				} else {
					ddl = property.getDataType().toUpperCase();
				}
			}
		}
		ddl += Util.getSpaces(ddl, 13);

		return ddl;
	}


	private String generateEnums(Graph graph) {
		String ddl = "";
		boolean first = true;
		
		for(Node node : graph.getNodes()) {
			for(NodeProperty property : node.getProperties()) {
				if(property instanceof NodePropertyEnumeration) {
					ddl += "CREATE TYPE " + property.getName() + "_enum_type AS ENUM ("; 
					first = true;
					
					for (String value : ((NodePropertyEnumeration) property).getValues()) {
						if(first) {
							ddl += "'" + value + "'";
							first = false;
						}
						else {
							ddl += ", '" + value + "'";
						}
					}
					ddl += "); \n";
				}
			}
			
		}
		if(ddl.length() > 1) {
			ddl += "\n\n";
		}
		
		return ddl;
	}
	
	//*************************************************************************************
	//** Method for generate the Triggers
	//*************************************************************************************
	public ArrayList<TriggerResult> generateTriggers(Graph graph, TraceTable traceTable) {
		PostgreTrigger postgreTrigger = new PostgreTrigger(graph, traceTable);
		return postgreTrigger.getTriggers();
	}
}
