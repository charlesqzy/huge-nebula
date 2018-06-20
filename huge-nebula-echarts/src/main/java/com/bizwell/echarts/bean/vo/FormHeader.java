package com.bizwell.echarts.bean.vo;

/**
 * @author zhangjianjun
 * @date 2018年6月14日
 *
 */
public class FormHeader {
	
	private String prop;
	
	private String label;

	public String getProp() {
		return prop;
	}

	public void setProp(String prop) {
		this.prop = prop;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return String.format("FormHeader [prop=%s, label=%s]", prop, label);
	}

}
