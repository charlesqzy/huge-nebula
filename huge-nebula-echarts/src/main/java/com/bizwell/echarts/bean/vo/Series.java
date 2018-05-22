package com.bizwell.echarts.bean.vo;

import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月22日
 *
 */
public class Series {
	
	private Object name;
	
	private String type;
	
	private String stack;
	
	private List<Object> data;
	
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

	public List<Object> getData() {
		return data;
	}

	public void setData(List<Object> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("Series [name=%s, type=%s, stack=%s, data=%s]", name, type, stack, data);
	}

}
