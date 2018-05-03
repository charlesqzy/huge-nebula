package com.bizwell.datasource.bean;

import java.util.List;
import java.util.Map;

public class SheetInfo {
	private String sheetName;
	private List<XLSHaderType> typeList;
	List<Map<String, String>> contentList;
	

	public SheetInfo() {
		super();
	}

	public SheetInfo(String sheetName, List<XLSHaderType> typeList, List<Map<String, String>> contentList) {
		super();
		this.sheetName = sheetName;
		this.typeList = typeList;
		this.contentList = contentList;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public List<XLSHaderType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<XLSHaderType> typeList) {
		this.typeList = typeList;
	}

	public List<Map<String, String>> getContentList() {
		return contentList;
	}

	public void setContentList(List<Map<String, String>> contentList) {
		this.contentList = contentList;
	}

}
