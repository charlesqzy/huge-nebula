package com.bizwell.echarts.bean.vo;

public class YAxis {
    private String type="value";
    private String name;
    private String splitLine;
//    private Integer min=0;
//    private Integer max=250;
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSplitLine() {
		return splitLine;
	}
	public void setSplitLine(String splitLine) {
		this.splitLine = splitLine;
	}

    
}
