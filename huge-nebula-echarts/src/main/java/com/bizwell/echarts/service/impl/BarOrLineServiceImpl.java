package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.bean.vo.Series;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.service.AbstractReportService;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
// 柱形图或者折线图模板,用于封装数据
@Service("04Service")
public class BarOrLineServiceImpl extends AbstractReportService {

	@Override
	protected ResultData setupData(List<Map<String, Object>> list, String data, Integer userId) {

		// 获取维度字段名称
		List<SheetMetaData> dimensions = JsonUtils.getFields(data, "dimension", "metadataId", userId);
		// 组装维度,假如有多个维度通过 "|"分隔
		List<Object> names = new ArrayList<Object>();
		for (Map<String, Object> map : list) {
			String name = new String();
			for (SheetMetaData sheetMetaData : dimensions) {
				name = name + map.get(sheetMetaData.getFieldColumn()) + "|";
			}
			if (name.endsWith("|")) {
				name = name.substring(0, name.length() - 1);
			}
			names.add(name);
		}
		
		// 通过json中的字段名称获取对应的值
		String type = JsonUtils.getString(data, "type");
		String stack = JsonUtils.getString(data, "stack");
		String echartType = JsonUtils.getString(data, "echartType");
		
		List<Series> seriesList = new ArrayList<Series>();
		List<String> legend = new ArrayList<String>();
		// 获取数值字段
		List<SheetMetaData> measures = JsonUtils.getFields(data, "measure1", "metadataId", userId);
		
		for (SheetMetaData sheetMetaData : measures) {
			List<Object> values = new ArrayList<Object>();
			for (Map<String, Object> map : list) {
				Object value = map.get(sheetMetaData.getFieldColumn());
				values.add(value);
			}
			
			String fieldName = sheetMetaData.getFieldNameNew();
			legend.add(fieldName);
			// 封装series
			Series series = new Series();
			series.setName(fieldName);
			series.setStack(stack);
			series.setData(values);
			if (type.equals("bar")) {
				series.setType(type);
			}
			if (type.equals("line")) {
				series.setType(type);
				series.setSmooth(true);
			}
			if (type.equals("area")) {
				series.setType("line");
				series.setSmooth(true);
				series.setAreaStyle("{normal: {}}");
			}
			seriesList.add(series);
		}
		
		// 封装结果数据
		ResultData resultData = new ResultData();
		resultData.setNames(names);
		resultData.setSeries(seriesList);
		resultData.setLegend(legend);
		resultData.setEchartType(echartType);
		return resultData;
	}
	
}
