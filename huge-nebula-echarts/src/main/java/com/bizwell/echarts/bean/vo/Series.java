package com.bizwell.echarts.bean.vo;

import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月22日
 *
 */
// 图表中series中所需的数据
public class Series {
	
	private Object name;
	
	private String type;
	
	private String stack;
	
	private Boolean smooth;
	
	private String  areaStyle;
	
	private Integer yAxisIndex;
	
	private List<Object> data;
	
	
	public Integer getyAxisIndex() {
		return yAxisIndex;
	}

	public void setyAxisIndex(Integer yAxisIndex) {
		this.yAxisIndex = yAxisIndex;
	}

	public Object getName() {
		return name;
	}

	public void setName(Object name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public Boolean getSmooth() {
		return smooth;
	}

	public void setSmooth(Boolean smooth) {
		this.smooth = smooth;
	}

	public String getAreaStyle() {
		return areaStyle;
	}

	public void setAreaStyle(String areaStyle) {
		this.areaStyle = areaStyle;
	}

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("Series [name=%s, type=%s, stack=%s, smooth=%s, areaStyle=%s , yAxisIndex=%s, data=%s]", name, type, stack,
				smooth, areaStyle, yAxisIndex, data);
	}

}
