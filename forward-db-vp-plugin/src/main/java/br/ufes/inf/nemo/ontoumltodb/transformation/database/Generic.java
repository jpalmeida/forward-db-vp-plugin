package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import br.ufes.inf.nemo.ontoumltodb.transformation.database.trigger.TriggerResult;
import br.ufes.inf.nemo.ontoumltodb.transformation.database.view.TraceSetQueryBuilder;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Graph;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodePropertyEnumeration;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceSet;
import br.ufes.inf.nemo.ontoumltodb.transformation.tracer.TraceTable;
import br.ufes.inf.nemo.ontoumltodb.util.IndexType;
import br.ufes.inf.nemo.ontoumltodb.util.Stereotype;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class Generic implements IDbms{
	protected Map<String, String> types;
	protected final String connectionName = "jdbc.name=ontouml2-db00-ufes-nemo-000000000002";
	private int indexNumber = 0;

	public Generic() {
		this.types = new HashMap<String, String>();
		this.types.put("boolean", "BIT");
		this.types.put("byte", "BIT(8)");
		this.types.put("char", "CHAR(3)");
		this.types.put("double", "DOUBLE");
		this.types.put("float", "FLOAT");
		this.types.put("int", "INTEGER");
		this.types.put("long", "BIGINT");
		this.types.put("short", "SMALLINT");
		this.types.put("string", "VARCHAR(20)");
	}
	

	public String getSchema(Graph graph) {
		String ddl = "";

		ddl = createTables(graph);

		ddl += createForeignKeys(graph);	
		
		ddl += createInserts(graph);
		
		return ddl;
	}
	
	public String getStringValue(Object value) {
		String text = value.toString();
		text = text.toString().toUpperCase();

		if (value instanceof String) {
			return "'" + text + "'";
		} else {
			return text;
		}
	}

	// ************************************************************************
	public String createTables(Graph graph) {
		String ddl = "";
		for (Node node : graph.getNodes()) {
			ddl += createTable(node);
		}
		return ddl;
	}

	public String createTable(Node node) {
		String ddl = "";
		boolean firstColumn = true;

		ddl += createTableDescription() + node.getName() + " ( ";

		for (NodeProperty property : node.getProperties()) {
			ddl += createColumn(property, firstColumn);
			firstColumn = false;
		}

		ddl += this.getConstraintTable(node);

		ddl += "\n); \n\n";
		return ddl;
	}

	public String createTableDescription() {
		return "CREATE TABLE ";
	}

	public String getConstraintTable(Node node) {
		return "";
	}

	public String createColumn(NodeProperty property, boolean firstColumn) {
		String ddl = "";
		String comma = "";
		String columnName = "";
		String columnType = "";
		String primaryKey = "";
		String nullable = "";
		String defaultValue = "";

		if (firstColumn)
			comma = "\n" + Util.getSpaces("", 8);
		else
			comma = "\n," + Util.getSpaces(",", 8);

		columnName = property.getName() + Util.getSpaces(property.getName(), 23);

		columnType = getColumnName(property);

		primaryKey = getPKDescription(property);

		nullable = getNullable(property);

		defaultValue = getDefaultValue(property);

		ddl += comma;
		ddl += columnName;
		ddl += columnType;
		ddl += nullable;
		ddl += primaryKey;
		ddl += defaultValue;

		return ddl;
	}

	public String getPKDescription(NodeProperty property) {
		if (property.isPrimaryKey())
			return " PRIMARY KEY";
		else
			return "";
	}

	public String getNullable(NodeProperty property) {
		if (property.isNullable())
			return " NULL";
		else
			return " NOT NULL";
	}

	public String getColumnName(NodeProperty property) {
		return this.getColumnType(property);
	}

	public String getDefaultValue(NodeProperty property) {
		if (property.getDefaultValue() != null)
			return (" DEFAULT " + property.getDefaultValue()).toUpperCase();
		else
			return "";
	}

	public String getColumnType(NodeProperty property) {
		String ddl = "";
		boolean first;

		if ( 	property instanceof NodePropertyEnumeration &&
				((NodePropertyEnumeration)property).isGenerateEnumColumn()	){
			ddl = "ENUM(";
			first = true;
			for (String value : ((NodePropertyEnumeration) property).getValues()) {
				if (first) {
					ddl += "'" + value + "'";
					first = false;
				} else {
					ddl += ",'" + value + "'";
				}
			}
			ddl += ")";
		} else {
			if (this.types.get(property.getDataType()) != null) {
				ddl = this.types.get(property.getDataType());
			} else {
				if(property.getDataType() != null) {
					ddl = property.getDataType().toUpperCase();
				}
				else {
					ddl = "int";
				}
			}
		}
		ddl += Util.getSpaces(ddl, 13);
		return ddl;
	}

	// ***************************************************************************

	public String createForeignKeys(Graph graph) {
		String ddl = "";

		for (Node node : graph.getNodes()) {
			for (NodeProperty property : node.getProperties()) {
				if (property.isForeignKey()) {
					ddl += "\n\nALTER TABLE ";
					ddl += node.getName();
					ddl += " ADD FOREIGN KEY ( ";
					ddl += property.getName();
					ddl += " ) REFERENCES ";
					ddl += graph.getNodeById(property.getForeignKeyNodeID()).getName();
					ddl += " ( ";
					ddl += graph.getNodeById(property.getForeignKeyNodeID()).getPKName();
					ddl += " );";
				}
			}
		}
		return ddl;
	}

	// ***************************************************************************
	
	public String getIndexes(Graph graph) {
		String text = "\n\n";

		for (Node node : graph.getNodes()) {
			indexNumber = 0;
			text += getIndexForFk(node);
			
			text += getCommonIndex(node);
			
			text += getUniqueIndexWithFk(node);
		}
		return text;
	}
	
	private ArrayList<NodeProperty> getIndexProperties(Node node, IndexType indexType){
		ArrayList<NodeProperty> result = new ArrayList<NodeProperty>();
		for (NodeProperty property : node.getProperties()) {
			if(property.getIndexType() == indexType) {
				result.add(property);
			}
		}
		return result;
	}
	
	private String getCommonIndex(Node node) {
		ArrayList<NodeProperty> properties = getIndexProperties(node, IndexType.INDEX);
		String text = "";
		
		if(properties.size() == 0)
			return "";
		
		for(NodeProperty property : properties) {
			indexNumber++;
			text += "CREATE INDEX ";
			text += "ix_" + node.getName() + "_" + indexNumber;
			text += " ON " + node.getName();
			text += " ( ";
		
			text += property.getName();
			text += ", ";
			text += node.getPKName();
			text += " );";
			text += "\n\n";
			
		}
		return text;
	}
	
	private String getUniqueIndexWithFk(Node node) {
		ArrayList<NodeProperty> properties = getIndexProperties(node, IndexType.UNIQUEINDEXSWITHFK);
		String text = "";
		String fkFieldName = "";
		
		if(properties.size() == 0)
			return "";
		
		for(NodeProperty property : properties) {
			indexNumber++;
			text += "CREATE UNIQUE INDEX ";
			text += "ix_" + node.getName() + "_" + indexNumber;
			text += " ON " + node.getName();
			text += " ( ";
		
			text += property.getName();
			text += ", ";
			fkFieldName = getFKFieldName(node);
			if (fkFieldName != "") {
				text += fkFieldName;
			}
			text += " );";
			text += "\n\n";
		}
		return text;
	}
	
	private String getIndexForFk(Node node) {
		String text = "";
		
		for (NodeProperty property : node.getProperties()) {
			if (property.isForeignKey()) {
				indexNumber++;
				text += "CREATE INDEX ";
				text += "ix_" + node.getName() + "_" + indexNumber;
				text += " ON " + node.getName();
				text += " ( ";
				text += property.getName();
				if(!property.getName().equals(node.getPKName())) {
					text += ", ";
					text += node.getPKName();
				}
				text += " );\n\n";
			}
		}
		
		return text;
	}

	public String getFKFieldName(Node node) {
		String fkNames = "";
		boolean first = true;

		for (NodeProperty property : node.getProperties()) {
			if (property.isForeignKey()) {
				if (!first) {
					fkNames += ", ";
				}
				fkNames += property.getName();
				first = false;
			}
		}
		return fkNames;
	}
	
	// *****************************************************************************************
	public String createInserts(Graph graph) {
		String ddl = "";
		int index = 1;
		for (Node node : graph.getNodes()) {
			if (node.getStereotype() == Stereotype.ENUMERATION) {
				for (NodeProperty property : node.getProperties()) {
					if (property instanceof NodePropertyEnumeration) {
						index = 1;
						for (String value : ((NodePropertyEnumeration) property).getValues()) {
							ddl += "INSERT INTO " + node.getName() + "("+ 
									node.getPrimaryKey().getName()+ ", "+ 
									property.getName() +" )"+ 
							" VALUES("+ 
									index + ", '"+ 
									value + "');\n";
							index++;
						}
						ddl += "\n";
					}
				}
			}
		}
		return ddl;
	}

	//*************************************************************************************
	//** Methods for generate the Views
	//*************************************************************************************
	
	public String getViewName(Node node) {
		 return "CREATE VIEW vw_" + node.getName() + " AS ";
	}
	
	public String generateViewForEachTrace(TraceTable traceTable, boolean putInheritedAttributes) {//, IDbms dbms) {	
		// String text = "";
		StringBuilder text = new StringBuilder();
		TraceSetQueryBuilder generateQuery2;		
		generateQuery2 = new TraceSetQueryBuilder(traceTable.getOriginalGraph(), traceTable, putInheritedAttributes);
		
		for (TraceSet traceSet : traceTable.getTraces()) {
			
			text.append(getViewName(traceSet.getSourceNode()));
			text.append("\n");
			
			text.append(generateQuery2.generateQueryForTraceSet(traceSet));
			text.append("\n");
		}
		return text.toString();
	}
	
	//*************************************************************************************
	//** Method for generate the Triggers
	//*************************************************************************************
	public ArrayList<TriggerResult> generateTriggers(Graph graph, TraceTable traceTable) {
		ArrayList<TriggerResult> result = new ArrayList<TriggerResult>();
		TriggerResult triggerResult = new TriggerResult();
		triggerResult.setName("Not implemented");
		triggerResult.setScript( "Not implemented. Must call a specific DBMS.");
		result.add(triggerResult);
		return result;
	}
}
