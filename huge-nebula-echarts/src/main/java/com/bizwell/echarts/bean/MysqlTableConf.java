package com.bizwell.echarts.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class MysqlTableConf {
	private Integer id;
	private Integer connId;
	private String connName;
	private String databaseName;
	private String tableName;
	private String tableComment;
	private Integer tableClumns;
	private Integer tableRows;
	private String rowsClumns;
	private Integer userId;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateTime;


	public Integer getTableClumns() {
		return tableClumns;
	}

	public void setTableClumns(Integer tableClumns) {
		this.tableClumns = tableClumns;
	}

	public Integer getTableRows() {
		return tableRows;
	}

	public void setTableRows(Integer tableRows) {
		this.tableRows = tableRows;
	}

	public String getRowsClumns() {
		return rowsClumns;
	}

	public void setRowsClumns(String rowsClumns) {
		this.rowsClumns = rowsClumns;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getConnId() {
		return connId;
	}

	public void setConnId(Integer connId) {
		this.connId = connId;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

}
