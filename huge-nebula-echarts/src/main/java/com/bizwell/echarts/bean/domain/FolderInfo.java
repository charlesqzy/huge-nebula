package com.bizwell.echarts.bean.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月14日
 *
 */
// 表gb_folder_info对应的实体类,用于仪表盘下面的文件夹
public class FolderInfo {
	
    private Integer id;
    
    private String panelUuid;

    private Integer userId;

    private String folderName;

    private Integer folderType;

    public String getPanelUuid() {
		return panelUuid;
	}

	public void setPanelUuid(String panelUuid) {
		this.panelUuid = panelUuid;
	}

	private Integer parentId;

    private Integer level;
    
    private Integer isDel;

    private Date createTime;

    private Date updateTime;

    private String reserved1;

    private String reserved2;

    private Boolean showMore;
    
    private Integer isHeaderShow;
    
    private List<ChildrenFolder> childrenFolder = new ArrayList<ChildrenFolder>();

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
        this.folderName = folderName == null ? null : folderName.trim();
    }

    public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<ChildrenFolder> getChildrenFolder() {
		return childrenFolder;
	}

	public void setChildrenFolder(List<ChildrenFolder> childrenFolder) {
		this.childrenFolder = childrenFolder;
	}

	public Integer getFolderType() {
		return folderType;
	}

	public void setFolderType(Integer folderType) {
		this.folderType = folderType;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getReserved1() {
		return reserved1;
	}

	public void setReserved1(String reserved1) {
		this.reserved1 = reserved1;
	}

	public String getReserved2() {
		return reserved2;
	}

	public void setReserved2(String reserved2) {
		this.reserved2 = reserved2;
	}

	public Boolean getShowMore() {
		return showMore;
	}

	public void setShowMore(Boolean showMore) {
		this.showMore = showMore;
	}
	
	public Integer getIsHeaderShow() {
		return isHeaderShow;
	}

	public void setIsHeaderShow(Integer isHeaderShow) {
		this.isHeaderShow = isHeaderShow;
	}

	@Override
	public String toString() {
		return String.format(
				"FolderInfo [id=%s, userId=%s, folderName=%s, folderType=%s, parentId=%s, level=%s, isDel=%s, createTime=%s, updateTime=%s, reserved1=%s, reserved2=%s, showMore=%s, childrenFolder=%s]",
				id, userId, folderName, folderType, parentId, level, isDel, createTime, updateTime, reserved1,
				reserved2, showMore, childrenFolder);
	}

}