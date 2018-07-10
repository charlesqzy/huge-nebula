package com.bizwell.echarts.bean.vo;

import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月22日
 *
 */
// 封装图表结构的类,用于前端echarts中数据展示
public class ResultData {
	
	private List<Object> names;
	
	private List<YAxis> yAxies;
	
	private List<Series> series;
	
	private List<String> legend;
	
	private String echartType;
	
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public List<Object> getNames() {
		return names;
	}

	public void setNames(List<Object> names) {
		this.names = names;
	}

	public List<Series> getSeries() {
		return series;
	}

	public void setSeries(List<Series> series) {
		this.series = series;
	}

	public List<String> getLegend() {
		return legend;
	}

	public void setLegend(List<String> legend) {
		this.legend = legend;
	}

	public String getEchartType() {
		return echartType;
	}

	public void setEchartType(String echartType) {
		this.echartType = echartType;
	}
	
	

	public List<YAxis> getyAxies() {
		return yAxies;
	}

	public void setyAxies(List<YAxis> yAxies) {
		this.yAxies = yAxies;
	}

	@Override
	public String toString() {
		return String.format("ResultData [names=%s , yAxies=%s, series=%s, legend=%s, echartType=%s, value=%s]", names, yAxies, series,
				legend, echartType, value);
	}

}
