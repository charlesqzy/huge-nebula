package com.bizwell.echarts.bean.dto;

import java.util.Arrays;

/**
 * @author zhangjianjun
 * @date 2018年5月3日
 *
 */
public class QueryParam {
	
	private String latitude;
	
	private String[] value1;
	
	private String[] value2;
	
	private String[] condition;
	
	private String tableName;

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String[] getValue1() {
		return value1;
	}

	public void setValue1(String[] value1) {
		this.value1 = value1;
	}

	public String[] getValue2() {
		return value2;
	}

	public void setValue2(String[] value2) {
		this.value2 = value2;
	}

	public String[] getCondition() {
		return condition;
	}

	public void setCondition(String[] condition) {
		this.condition = condition;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String toString() {
		return String.format("QueryParam [latitude=%s, value1=%s, value2=%s, condition=%s, tableName=%s]", latitude,
				Arrays.toString(value1), Arrays.toString(value2), Arrays.toString(condition), tableName);
	}
	
}
