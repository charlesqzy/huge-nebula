package com.bizwell.datasource.bean;

import java.util.ArrayList;
import java.util.List;

public class DatabaseInfo {

	private String databaseName;
	
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
