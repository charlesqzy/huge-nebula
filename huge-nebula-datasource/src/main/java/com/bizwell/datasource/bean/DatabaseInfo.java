package com.bizwell.datasource.bean;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInfo {

	private String databaseName;
	private Integer connId;
	private String connName;
	
	
	
	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public Integer getConnId() {
		return connId;
	}

	public void setConnId(Integer connId) {
		this.connId = connId;
	}

	private List<String> tableNames = new ArrayList();

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public List<String> getTableNames() {
		return tableNames;
	}

	public void setTableNames(List<String> tableNames) {
		this.tableNames = tableNames;
	}
	
	
}
