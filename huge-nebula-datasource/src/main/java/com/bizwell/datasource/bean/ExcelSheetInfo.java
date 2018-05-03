package com.bizwell.datasource.bean;

import java.util.Date;

public class ExcelSheetInfo {
	private Integer id;
	private Integer excelFileId;
	private String sheetName;
	private Integer folderId;
	private Integer categoryFlag;
	private String remark;
	private String tableName;
	private Date updateTime;
	private Integer userId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getExcelFileId() {
		return excelFileId;
	}

	public void setExcelFileId(Integer excelFileId) {
		this.excelFileId = excelFileId;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public Integer getFolderId() {
		return folderId;
	}

	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}

	public Integer getCategoryFlag() {
		return categoryFlag;
	}

	public void setCategoryFlag(Integer categoryFlag) {
		this.categoryFlag = categoryFlag;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
