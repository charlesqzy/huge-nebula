package com.bizwell.echarts.bean.dto;

/**
 * @author zhangjianjun
 * @date 2018年5月16日
 *
 */
public class FolderParam {
	
	private Integer id;
	
	private Integer userId;

	private Integer parentId;
	
	private Integer childId;
	
	private String folderName;
	
	private Integer level;

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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return String.format("FolderParam [id=%s, userId=%s, parentId=%s, childId=%s, folderName=%s, level=%s]", id,
				userId, parentId, childId, folderName, level);
	}
	
}
