package com.bizwell.echarts.bean.domain;

import java.util.Date;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
// 表ws_chart_config_info对应的实体类
public class ChartConfig {
	
    private Integer id;
    
    private Integer sheetId;

    private Integer folderId;

    private Integer panelId;
    
    private String panelUuid;

    private Integer userId;

    private String chartName;

    private String chartRemarks;

    private String location;

    private String sqlConfig;

    private String chatConfig;

    private Date createTime;

    private Date updateTime;

    private String reserved1;

    private String reserved2;

    private String reserved3;

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
        this.chartName = chartName == null ? null : chartName.trim();
    }

    public String getChartRemarks() {
        return chartRemarks;
    }

    public void setChartRemarks(String chartRemarks) {
        this.chartRemarks = chartRemarks == null ? null : chartRemarks.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getSqlConfig() {
        return sqlConfig;
    }

    public void setSqlConfig(String sqlConfig) {
        this.sqlConfig = sqlConfig == null ? null : sqlConfig.trim();
    }

    public String getChatConfig() {
        return chatConfig;
    }

    public void setChatConfig(String chatConfig) {
        this.chatConfig = chatConfig == null ? null : chatConfig.trim();
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
        this.reserved1 = reserved1 == null ? null : reserved1.trim();
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2 == null ? null : reserved2.trim();
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3 == null ? null : reserved3.trim();
    }

    
	public String getPanelUuid() {
		return panelUuid;
	}

	public void setPanelUuid(String panelUuid) {
		this.panelUuid = panelUuid;
	}

	@Override
	public String toString() {
		return String.format(
				"ChartConfig [id=%s, sheetId=%s, folderId=%s, panelId=%s,panelUuid=%s, userId=%s, chartName=%s, chartRemarks=%s, location=%s, sqlConfig=%s, chatConfig=%s, createTime=%s, updateTime=%s, reserved1=%s, reserved2=%s, reserved3=%s]",
				id, sheetId, folderId, panelId,panelUuid, userId, chartName, chartRemarks, location, sqlConfig, chatConfig,
				createTime, updateTime, reserved1, reserved2, reserved3);
	}
    
}