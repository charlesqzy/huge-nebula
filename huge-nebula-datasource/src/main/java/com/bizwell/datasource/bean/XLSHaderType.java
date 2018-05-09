package com.bizwell.datasource.bean;

public class XLSHaderType {
	private String prop;
	private String type;
	private String label;
	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getProp() {
		return prop;
	}
	public void setProp(String prop) {
		this.prop = prop;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public XLSHaderType(String prop, String type) {
		super();
		this.prop = prop;
		this.type = type;
	}
	
	

	public XLSHaderType() {
		super();
	}
}
