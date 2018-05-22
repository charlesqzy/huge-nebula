package com.bizwell.echarts.bean.vo;

import java.util.List;

/**
 * @author zhangjianjun
 * @date 2018年5月22日
 *
 */
public class ResultData {
	
	private List<String> names;
	
	private List<Series> series;
	
	private List<String> legend;

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
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

	@Override
	public String toString() {
		return String.format("ResultData [names=%s, series=%s, legend=%s]", names, series, legend);
	}

}
