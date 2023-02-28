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
		StringBuilder ddl = new StringBuilder();;

		ddl.append(createTables(graph));

		ddl.append(createForeignKeys(graph));	
		
		ddl.append(createInserts(graph));
		
		ddl.append(createUniqueIndexs(graph)); //this index must ensure the correct source model rules.
		
		return ddl.toString();
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
		StringBuilder ddl = new StringBuilder();
		for (Node node : graph.getNodes()) {
			ddl.append(createTable(node));
		}
		return ddl.toString();
	}

	public String createTable(Node node) {
		StringBuilder ddl = new StringBuilder();
		boolean firstColumn = true;

		ddl.append(createTableDescription());
		ddl.append(node.getName());
		ddl.append(" ( ");

		for (NodeProperty property : node.getProperties()) {
			ddl.append(createColumn(property, firstColumn));
			firstColumn = false;
		}

		ddl.append(this.getConstraintTable(node));

		ddl.append("\n); \n\n");
		return ddl.toString();
	}

	public String createTableDescription() {
		return "CREATE TABLE ";
	}

	public String getConstraintTable(Node node) {
		return "";
	}

	public String createColumn(NodeProperty property, boolean firstColumn) {
		StringBuilder ddl = new StringBuilder();
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

		columnName = getColumnName(property);// + Util.getSpaces(property.getName(), 23);

		columnType = getColumnType(property);

		primaryKey = getPKDescription(property);

		nullable = getNullable(property);

		defaultValue = getDefaultValue(property);

		ddl.append(comma);
		ddl.append(columnName);
		ddl.append(Util.getSpaces(columnName, 25));
		ddl.append(columnType);
		ddl.append(nullable);
		ddl.append(primaryKey);
		ddl.append(defaultValue);

		return ddl.toString();
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
		return property.getName() + Util.getSpaces(property.getName(), 23);
	}

	public String getDefaultValue(NodeProperty property) {
		if (property.getDefaultValue() != null)
			return (" DEFAULT " + property.getDefaultValue()).toUpperCase();
		else
			return "";
	}

	public String getColumnType(NodeProperty property) {
		StringBuilder ddl = new StringBuilder();
		boolean first;

		if ( 	property instanceof NodePropertyEnumeration &&
				((NodePropertyEnumeration)property).isGenerateEnumColumn()	){
			ddl.append("ENUM(");
			first = true;
			for (String value : ((NodePropertyEnumeration) property).getValues()) {
				if (first) {
					ddl.append("'");
					ddl.append(value);
					ddl.append("'");
					first = false;
				} else {
					ddl.append(",'");
					ddl.append(value);
					ddl.append("'");
				}
			}
			ddl.append(")");
		} else {
			if (property.getDataType() == null) {
				ddl.append("INT");
			} else {
				if(this.types.get(property.getDataType().toLowerCase()) != null) {
					ddl.append(this.types.get(property.getDataType().toLowerCase()));
				}
				else {
					ddl.append(property.getDataType().toUpperCase());
				}
			}
//			if (this.types.get(property.getDataType().toLowerCase()) != null) {
//				ddl = this.types.get(property.getDataType().toLowerCase());
//			} else {
//				if(property.getDataType() != null) {
//					ddl = property.getDataType().toUpperCase();
//				}
//				else {
//					ddl = "INT";
//				}
//			}
		}
		ddl.append(Util.getSpaces(ddl.toString(), 13));
		return ddl.toString();
	}

	// ***************************************************************************

	public String createForeignKeys(Graph graph) {
		StringBuilder ddl = new StringBuilder();

		for (Node node : graph.getNodes()) {
			for (NodeProperty property : node.getProperties()) {
				if (property.isForeignKey()) {
					ddl.append("\n\nALTER TABLE ");
					ddl.append(node.getName());
					ddl.append(" ADD FOREIGN KEY ( ");
					ddl.append(property.getName());
					ddl.append(" ) REFERENCES ");
					ddl.append(graph.getNodeById(property.getForeignKeyNodeID()).getName());
					ddl.append(" ( ");
					ddl.append(graph.getNodeById(property.getForeignKeyNodeID()).getPKName());
					ddl.append(" );");
				}
			}
		}
		return ddl.toString();
	}
	
	private String createUniqueIndexs(Graph graph) {
		StringBuilder text = new StringBuilder();
		text.append("\n\n");
		
		for (Node node : graph.getNodes()) {
			text.append(getUniqueIndex(node));
		}
		
		return text.toString();
	}

	// ***************************************************************************
	
	public String getIndexes(Graph graph) {
		StringBuilder text = new StringBuilder();
		text.append("\n\n");

		for (Node node : graph.getNodes()) {
			indexNumber = 0;
			text.append(getIndexForFk(node));
			
			text.append(getCommonIndex(node));
			
			text.append(getUniqueIndexWithFk(node));
		}
		return text.toString();
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
		StringBuilder text = new StringBuilder();
		
		if(properties.size() == 0)
			return "";
		
		for(NodeProperty property : properties) {
			indexNumber++;
			text.append("CREATE INDEX ");
			text.append("ix_");
			text.append(node.getName());
			text.append("_");
			text.append(indexNumber);
			text.append(" ON ");
			text.append(node.getName());
			text.append(" ( ");
		
			text.append(property.getName());
			text.append(", ");
			text.append(node.getPKName());
			text.append(" );");
			text.append("\n\n");
			
		}
		return text.toString();
	}
	
	private String getUniqueIndexWithFk(Node node) {
		ArrayList<NodeProperty> properties = getIndexProperties(node, IndexType.UNIQUEINDEXSWITHFK);
		StringBuilder text = new StringBuilder();
		String fkFieldName = "";
		
		if(properties.size() == 0)
			return "";
		
		for(NodeProperty property : properties) {
			indexNumber++;
			text.append("CREATE UNIQUE INDEX ");
			text.append("ix_");
			text.append(node.getName());
			text.append("_");
			text.append(indexNumber);
			text.append(" ON ");
			text.append(node.getName());
			text.append(" ( ");
		
			text.append(property.getName());
			text.append(", ");
			fkFieldName = getFKFieldName(node);
			if (fkFieldName != "") {
				text.append(fkFieldName);
			}
			text.append(" );");
			text.append("\n\n");
		}
		return text.toString();
	}
	
	private String getUniqueIndex(Node node) {
		ArrayList<NodeProperty> properties = getIndexProperties(node, IndexType.UNIQUEINDEX);
		StringBuilder text = new StringBuilder();
		
		if(properties.size() == 0)
			return "";
		
		for(NodeProperty property : properties) {
			indexNumber++;
			text.append("CREATE UNIQUE INDEX ");
			text.append("ix_");
			text.append(node.getName());
			text.append("_");
			text.append(indexNumber);
			text.append(" ON ");
			text.append(node.getName());
			text.append(" ( ");
			text.append(property.getName());
			text.append(" );\n\n");
		}
		return text.toString();
	}
	
	private String getIndexForFk(Node node) {
		StringBuilder text = new StringBuilder();
		
		for (NodeProperty property : node.getProperties()) {
			if (property.isForeignKey()) {
				indexNumber++;
				text.append("CREATE INDEX ");
				text.append("ix_");
				text.append(node.getName());
				text.append("_");
				text.append(indexNumber);
				text.append(" ON ");
				text.append(node.getName());
				text.append(" ( ");
				text.append(property.getName());
				if(!property.getName().equals(node.getPKName())) {
					text.append(", ");
					text.append(node.getPKName());
				}
				text.append(" );\n\n");
			}
		}
		return text.toString();
	}

	public String getFKFieldName(Node node) {
		StringBuilder fkNames = new StringBuilder();
		boolean first = true;

		for (NodeProperty property : node.getProperties()) {
			if (property.isForeignKey()) {
				if (!first) {
					fkNames.append(", ");
				}
				fkNames.append(property.getName());
				first = false;
			}
		}
		return fkNames.toString();
	}
	
	// *****************************************************************************************
	public String createInserts(Graph graph) {
		StringBuilder ddl = new StringBuilder();
		int index = 1;
		for (Node node : graph.getNodes()) {
			if (node.getStereotype() == Stereotype.ENUMERATION) {
				for (NodeProperty property : node.getProperties()) {
					if (property instanceof NodePropertyEnumeration) {
						index = 1;
						for (String value : ((NodePropertyEnumeration) property).getValues()) {
							ddl.append("INSERT INTO ");
							ddl.append(node.getName());
							ddl.append("("); 
							ddl.append(node.getPrimaryKey().getName());
							ddl.append(", "); 
							ddl.append(property.getName());
							ddl.append(" )"); 
							ddl.append(" VALUES("); 
							ddl.append(index);
							ddl.append(", '"); 
							ddl.append(value);
							ddl.append("');\n");
							index++;
						}
						ddl.append("\n");
					}
				}
			}
		}
		return ddl.toString();
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
