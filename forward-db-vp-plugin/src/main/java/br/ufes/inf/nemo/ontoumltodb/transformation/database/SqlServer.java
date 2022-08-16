package br.ufes.inf.nemo.ontoumltodb.transformation.database;

import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;

public class SqlServer extends Generic implements IDbms {

	public SqlServer() {
		super();
		this.types.put("boolean", "BIT");
		this.types.put("byte", "BINARY(8)");
		this.types.put("double", "REAL");
		this.types.put("float", "FLOAT(53)");
	}

	public String createTableDescription() {
		return "CREATE TABLE ";
	}

	public String getPKDescription(NodeProperty property) {
		if (property.isPrimaryKey()) {
			if (property.isPrimaryKeyAutoIncrement())
				return " IDENTITY(1,1) PRIMARY KEY";
			return " PRIMARY KEY";
		}
		return "";
	}
}
