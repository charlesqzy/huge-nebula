package com.bizwell.datasource.bean;

public class FolderInfo {
	private Integer id;
	private Integer userId;
	private String folderName;
	private Integer folderType;
	private Integer parentId;
	private Integer level;
	
	private Integer mode = 1;//1,excel文件夹    2,mysql数据库
	
	

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Integer getFolderType() {
		return folderType;
	}

	public void setFolderType(Integer folderType) {
		this.folderType = folderType;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

}
