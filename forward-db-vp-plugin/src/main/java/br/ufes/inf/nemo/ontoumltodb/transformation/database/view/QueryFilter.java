package br.ufes.inf.nemo.ontoumltodb.transformation.database.view;

public class QueryFilter {
	
	private String tableName;
	private String columnName;
	private Object value;
	private boolean applied;

	public QueryFilter(String tableName, String columnName, Object value) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.value = value;
		this.applied = false;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	public boolean isApplied() {
		return this.applied;
	}
	
	public void setApplied(boolean applied) {
		this.applied = applied;
	}
	
}
