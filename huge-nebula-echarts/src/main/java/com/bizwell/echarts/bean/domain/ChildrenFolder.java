package com.bizwell.echarts.bean.domain;

/**
 * @author zhangjianjun
 * @date 2018年5月14日
 *
 */
// 用于仪表盘下面的文件夹里面的文件,属于二级目录
public class ChildrenFolder {
	
	private Integer childId;

    private String childFolderName;
    
    private Boolean showMore1;

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

	public Boolean getShowMore1() {
		return showMore1;
	}

	public void setShowMore1(Boolean showMore1) {
		this.showMore1 = showMore1;
	}

	@Override
	public String toString() {
		return String.format("ChildrenFolder [childId=%s, childFolderName=%s, showMore1=%s]", childId, childFolderName,
				showMore1);
	}

}
