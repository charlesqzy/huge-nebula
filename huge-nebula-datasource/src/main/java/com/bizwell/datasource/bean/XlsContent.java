package com.bizwell.datasource.bean;

public class XlsContent {

	private String fileName;
	private String fileCode;
	private SheetInfo[] sheets;
	private Integer fileRows;
	private Integer fileColumns;
	private Integer excelFileId;
	private Integer userId;
	
	
	
	
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getFileCode() {
		return fileCode;
	}
	public void setFileCode(String fileCode) {
		this.fileCode = fileCode;
	}

	public Integer getExcelFileId() {
		return excelFileId;
	}
	public void setExcelFileId(Integer excelFileId) {
		this.excelFileId = excelFileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public SheetInfo[] getSheets() {
		return sheets;
	}
	public void setSheets(SheetInfo[] sheets) {
		this.sheets = sheets;
	}
	public Integer getFileRows() {
		return fileRows;
	}
	public void setFileRows(Integer fileRows) {
		this.fileRows = fileRows;
	}
	public Integer getFileColumns() {
		return fileColumns;
	}
	public void setFileColumns(Integer fileColumns) {
		this.fileColumns = fileColumns;
	}
	
	
	
	
	
	
}
