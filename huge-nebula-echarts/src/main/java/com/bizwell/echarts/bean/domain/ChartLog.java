package com.bizwell.echarts.bean.domain;

import java.util.Date;

public class ChartLog {
	private Integer id;
	private Integer panelId;
	private String panelUuid;
	private Integer userId;
	private Date createTime;
	
	

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPanelId() {
		return panelId;
	}

	public void setPanelId(Integer panelId) {
		this.panelId = panelId;
	}

	public String getPanelUuid() {
		return panelUuid;
	}

	public void setPanelUuid(String panelUuid) {
		this.panelUuid = panelUuid;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
