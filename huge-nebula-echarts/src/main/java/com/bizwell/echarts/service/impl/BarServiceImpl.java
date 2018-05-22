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
@Service("01Service")
public class BarServiceImpl extends AbstractReportService {

	@Override
	protected ResultData setupData(List<Map<String, Object>> list, String data) {

		// 获取维度字段名称
		List<SheetMetaData> dimensions = JsonUtils.getFields(data, "dimension", "metadataId");
		// 组装维度
		List<String> names = new ArrayList<String>();
		for (Map<String, Object> map : list) {
			String name = new String();
			for (SheetMetaData sheetMetaData : dimensions) {
				name = name + map.get(sheetMetaData.getFieldColumn());
			}
			names.add(name);
		}
		
		List<Series> seriesList = new ArrayList<Series>();
		List<String> legend = new ArrayList<String>();
		// 获取数值字段
		List<SheetMetaData> measures = JsonUtils.getFields(data, "measure1", "metadataId");
		
		for (SheetMetaData sheetMetaData : measures) {
			List<Object> values = new ArrayList<Object>();
			for (Map<String, Object> map : list) {
				Object value = map.get(sheetMetaData.getFieldColumn());
				values.add(value);
			}
			
			String fieldName = sheetMetaData.getFieldNameNew();
			legend.add(fieldName);
			Series series = new Series();
			series.setName(fieldName);
			series.setType("bar");
			series.setData(values);
			seriesList.add(series);
		}
		
		ResultData resultData = new ResultData();
		resultData.setNames(names);
		resultData.setSeries(seriesList);
		resultData.setLegend(legend);
		return resultData;
	}
	
}
