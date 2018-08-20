package com.bizwell.echarts.bean.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月30日
 *
 */
// 用于封装仪表盘展示页中各图表的位置信息与所需数据
public class ResultLocation {
	
	private Integer isHeaderShow;
	
	private String status;
	
	private String shareRemarks;
	
	private List<Object> locations = new ArrayList<Object>();

	public String getShareRemarks() {
		return shareRemarks;
	}

	public void setShareRemarks(String shareRemarks) {
		this.shareRemarks = shareRemarks;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Object> getLocations() {
		return locations;
	}

	public void setLocations(List<Object> locations) {
		this.locations = locations;
	}


	public Integer getIsHeaderShow() {
		return isHeaderShow;
	}

	public void setIsHeaderShow(Integer isHeaderShow) {
		this.isHeaderShow = isHeaderShow;
	}

	@Override
	public String toString() {
		return String.format("ResultLocation [status=%s, shareRemarks=%s, locations=%s]", status, shareRemarks,
				locations);
	}

}
