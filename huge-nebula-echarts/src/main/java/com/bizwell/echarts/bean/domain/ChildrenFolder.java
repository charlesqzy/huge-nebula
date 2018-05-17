package com.bizwell.echarts.bean.domain;

/**
 * @author zhangjianjun
 * @date 2018年5月14日
 *
 */
public class ChildrenFolder {
	
	private Integer childId;

    private String childFolderName;
    
    private Boolean showMore = false;

	public Integer getChildId() {
		return childId;
	}

	public void setChildId(Integer childId) {
		this.childId = childId;
	}

	public String getChildFolderName() {
		return childFolderName;
	}

	public void setChildFolderName(String childFolderName) {
		this.childFolderName = childFolderName;
	}

	public Boolean getShowMore() {
		return showMore;
	}

	public void setShowMore(Boolean showMore) {
		this.showMore = showMore;
	}

	@Override
	public String toString() {
		return String.format("ChildrenFolder [childId=%s, childFolderName=%s, showMore=%s]", childId, childFolderName,
				showMore);
	}

}
