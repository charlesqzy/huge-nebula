package com.bizwell.datasource.bean;

public class SheetMetadata {
	private Integer id;
	private Integer sheetId;
	private String tableName;
	private String fieldColumn;
	private String fieldNameOld;
	private String fieldNameNew;
	private Integer fieldType;
	private String fieldComment;
	private Integer isVisible;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSheetId() {
		return sheetId;
	}

	public void setSheetId(Integer sheetId) {
		this.sheetId = sheetId;
	}


	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getFieldNameOld() {
		return fieldNameOld;
	}

	public void setFieldNameOld(String fieldNameOld) {
		this.fieldNameOld = fieldNameOld;
	}

	public String getFieldNameNew() {
		return fieldNameNew;
	}

	public void setFieldNameNew(String fieldNameNew) {
		this.fieldNameNew = fieldNameNew;
	}

	public Integer getFieldType() {
		return fieldType;
	}

	public void setFieldType(Integer fieldType) {
		this.fieldType = fieldType;
	}

	public String getFieldComment() {
		return fieldComment;
	}

	public void setFieldComment(String fieldComment) {
		this.fieldComment = fieldComment;
	}

	public Integer getIsVisible() {
		return isVisible;
	}

	public void setIsVisible(Integer isVisible) {
		this.isVisible = isVisible;
	}

	public String getFieldColumn() {
		return fieldColumn;
	}

	public void setFieldColumn(String fieldColumn) {
		this.fieldColumn = fieldColumn;
	}

}
