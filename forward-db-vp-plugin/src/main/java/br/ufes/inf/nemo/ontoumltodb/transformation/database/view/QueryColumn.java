package br.ufes.inf.nemo.ontoumltodb.transformation.database.view;


public class QueryColumn {
	
	private String tableName;
	private String columnName;
	private String nickname;
	
	public QueryColumn(String tableName, String columnName, String nickname) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.nickname = nickname;
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

	public String getNickname() {
		return this.nickname;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public boolean existsColumn(String tableName, String columnName,  String nickname) {
		if(	existsTableName(tableName) && existsColumnName(columnName) && existsNickname (nickname))
			return true;
		
		return false;
	}
	
	public boolean existsTableName(String tableName) {
		if(	this.tableName.equals(tableName) )
			return true;
		
		return false;
	}
	
	public boolean existsColumnName(String columnName) {
		if(	this.columnName.equals(columnName) )
			return true;
		
		return false;
	}
	
	public boolean existsNickname(String nickname) {
		if(	this.nickname.equals(nickname) )
			return true;
		
		return false;
	}
}
