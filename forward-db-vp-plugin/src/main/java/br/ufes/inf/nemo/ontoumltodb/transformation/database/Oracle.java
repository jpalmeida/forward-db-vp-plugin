package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.Node;
import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;
import br.ufes.inf.nemo.ontoumltodb.util.Util;

public class Oracle extends Generic implements IDbms {

	public Oracle() {
		super();
		this.types.put("boolean", "CHAR(1)");
		this.types.put("byte", "RAW(1)");
		this.types.put("char", "CHAR(3)");
		this.types.put("double", "NUMBER(20,4)");
		this.types.put("float", "NUMBER(10,2)");
		this.types.put("int", "NUMBER(10,0)");
		this.types.put("long", "NUMBER(20,0)");
		this.types.put("short", "NUMBER(3,0)");
		this.types.put("string", "VARCHAR2(20)");
	}

	public String createTableDescription() {
		return "CREATE TABLE ";
	}

	public String getPKDescription(NodeProperty property) {
		if (property.isPrimaryKey() && property.isPrimaryKeyAutoIncrement()) {
			return " GENERATED ALWAYS AS IDENTITY ";
		}
		return "";
	}

	public String getConstraintTable(Node node) {
		return ("\n," + Util.getSpaces(",", 8) + "CONSTRAINT pk_" + node.getName() + " PRIMARY KEY( "
				+ node.getPrimaryKey().getName() + " )");
	}

}
