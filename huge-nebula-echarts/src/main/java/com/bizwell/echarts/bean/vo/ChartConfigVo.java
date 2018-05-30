package com.bizwell.echarts.bean.vo;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
public class ChartConfigVo {
	
	private Integer id;

    private Integer sheetId;

    private Integer folderId;

    private Integer panelId;

    private Integer userId;

    private String chartName;

    private String chartRemarks;

    private String location;

    private String sqlConfig;
    
    private String chatConfig;
    
    private String type;

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

	public Integer getFolderId() {
		return folderId;
	}

	public void setFolderId(Integer folderId) {
		this.folderId = folderId;
	}

	public Integer getPanelId() {
		return panelId;
	}

	public void setPanelId(Integer panelId) {
		this.panelId = panelId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getChartName() {
		return chartName;
	}

	public void setChartName(String chartName) {
		this.chartName = chartName;
	}

	public String getChartRemarks() {
		return chartRemarks;
	}

	public void setChartRemarks(String chartRemarks) {
		this.chartRemarks = chartRemarks;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSqlConfig() {
		return sqlConfig;
	}

	public void setSqlConfig(String sqlConfig) {
		this.sqlConfig = sqlConfig;
	}

	public String getChatConfig() {
		return chatConfig;
	}

	public void setChatConfig(String chatConfig) {
		this.chatConfig = chatConfig;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return String.format(
				"ChartConfigVo [id=%s, sheetId=%s, folderId=%s, panelId=%s, userId=%s, chartName=%s, chartRemarks=%s, location=%s, sqlConfig=%s, chatConfig=%s, type=%s]",
				id, sheetId, folderId, panelId, userId, chartName, chartRemarks, location, sqlConfig, chatConfig, type);
	}

}
