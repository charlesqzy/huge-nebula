package com.bizwell.echarts.bean.vo;

import java.util.ArrayList;
import java.util.List;

import com.bizwell.echarts.bean.domain.ChildrenFolder;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
 *
 */
// 封装FolderInfo,用于给前端展示
public class FolderVo {
	
	private Integer id;

	private String folderName;

	private List<ChildrenFolder> childrenFolder = new ArrayList<ChildrenFolder>();
	
	private Boolean showMore;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public List<ChildrenFolder> getChildrenFolder() {
		return childrenFolder;
	}

	public void setChildrenFolder(List<ChildrenFolder> childrenFolder) {
		this.childrenFolder = childrenFolder;
	}

	public Boolean getShowMore() {
		return showMore;
	}

	public void setShowMore(Boolean showMore) {
		this.showMore = showMore;
	}

	@Override
	public String toString() {
		return String.format("FolderVo [id=%s, folderName=%s, childrenFolder=%s, showMore=%s]", id, folderName,
				childrenFolder, showMore);
	}
	
}
