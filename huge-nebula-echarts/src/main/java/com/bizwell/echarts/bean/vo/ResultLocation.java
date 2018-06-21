package com.bizwell.echarts.bean.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月30日
 *
 */
public class ResultLocation {
	
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

	@Override
	public String toString() {
		return String.format("ResultLocation [status=%s, shareRemarks=%s, locations=%s]", status, shareRemarks,
				locations);
	}

}