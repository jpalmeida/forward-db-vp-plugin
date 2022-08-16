package br.ufes.inf.nemo.ontoumltodb.transformation.database;


import br.ufes.inf.nemo.ontoumltodb.transformation.graph.NodeProperty;

public class H2 extends Generic implements IDbms{

	public H2() {
		super();
		this.types.put("boolean", "BOOLEAN");
		this.types.put("byte", "BINARY(8)");
	}

//	public String createTableDescription() {
//		return "CREATE TABLE IF NOT EXISTS ";
//	}

	public String getPKDescription(NodeProperty property) {
		if (property.isPrimaryKey()) {
			if (property.isPrimaryKeyAutoIncrement())
				return " IDENTITY PRIMARY KEY";
			return " PRIMARY KEY";
		}
		return "";
	}
}
