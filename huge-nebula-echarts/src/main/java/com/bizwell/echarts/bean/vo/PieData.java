package com.bizwell.echarts.bean.vo;

/**
 * @author zhangjianjun
 * @date 2018年6月12日
 *
 */
// 用于封装饼图数据
public class PieData extends Object {
	
	private String name;
	
	private Object value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return String.format("PieData [name=%s, value=%s]", name, value);
	}

}
