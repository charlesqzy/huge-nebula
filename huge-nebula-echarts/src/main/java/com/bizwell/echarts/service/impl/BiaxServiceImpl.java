package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.bean.vo.Series;
import com.bizwell.echarts.bean.vo.YAxis;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.service.AbstractReportService;
import com.bizwell.echarts.service.SheetMetaDataService;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
// 双轴图模板,用于封装数据
@Service("06Service")
public class BiaxServiceImpl extends AbstractReportService {
	
	@Autowired
	private SheetMetaDataService sheetMetaDataService;

	@Override
	protected ResultData setupData(List<Map<String, Object>> list, String data, Integer userId) {

		List<Object> names = new ArrayList<Object>();
		for (Map<String, Object> map : list) {
			String name = new String();
			for(String key :map.keySet()){
				if(key.startsWith("D")){
					name = name + map.get(key) + "|";
				}
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
		
		Series series;
		
		YAxis yAxis = null;
		List<YAxis> yAxies = new ArrayList<YAxis>();
		
		//获取axiosType
		JSONArray axiosType = JsonUtils.getJSONArray(data, "axiosType");
		
		
		// 获取数值字段1
		List<SheetMetaData> measures = sheetMetaDataService.getFields(data, "measure1", "metadataId");
		
		if(measures.size()==0){			
			series = new Series();
			series.setType("bar");
			series.setName("");
			series.setStack("");
			series.setData(null);
			seriesList.add(series);
			
			yAxis = new YAxis();
			yAxis.setName("");
			yAxis.setSplitLine("");
			yAxies.add(yAxis);
		}
		
		for (SheetMetaData sheetMetaData : measures) {			
			List<Object> values = new ArrayList<Object>();
			//String aggregate = "";
			for (Map<String, Object> map : list) {				
				for(String key :map.keySet()){
					String[] split = key.split("_");
					if(key.startsWith("M1") && sheetMetaData.getFieldColumn().equals(split[1])){
						values.add(map.get(key));break;
						//aggregate = ReportManager.getAggregate(key);
					}
				}
			}
			String fieldName = sheetMetaData.getFieldNameNew();
			legend.add(fieldName);
			// 封装series
			series = new Series();
			series.setName(fieldName);
			series.setStack(stack);
			series.setData(values);
			
			
			if ("0".equals(axiosType.get(0).toString())) {
				series.setType("bar");
			}
			if ("1".equals(axiosType.get(0).toString())) {
				series.setType("line");
				series.setSmooth(true);
			}
			if ("2".equals(axiosType.get(0).toString())) {
				series.setType("bar");
				series.setSmooth(true);
				series.setStack("tow");
				series.setAreaStyle("{normal: {}}");
			}
			series.setyAxisIndex(0);
			seriesList.add(series);
		}
		
		if(measures.size()>0){
			yAxis = new YAxis();
			String name = measures.get(0).getFieldNameNew();
			yAxis.setName(name);
			yAxis.setSplitLine("{show: false}");
			yAxies.add(yAxis);
		}
		
		
		
		// 获取数值字段2
		List<SheetMetaData> measures2 = sheetMetaDataService.getFields(data, "measure2", "metadataId");
		for (SheetMetaData sheetMetaData : measures2) {			
			List<Object> values = new ArrayList<Object>();
			//String aggregate = "";
			for (Map<String, Object> map : list) {				
				for(String key :map.keySet()){
					String[] split = key.split("_");
					if(key.startsWith("M2") && sheetMetaData.getFieldColumn().equals(split[1])){
						values.add(map.get(key));break;
						//aggregate = ReportManager.getAggregate(key);
					}
				}
			}
			String fieldName = sheetMetaData.getFieldNameNew();
			legend.add(fieldName);
			// 封装series
			series = new Series();
			series.setName(fieldName);
			series.setStack(stack);
			series.setData(values);
			if ("0".equals(axiosType.get(1).toString())) {
				series.setType("bar");
			}
			if ("1".equals(axiosType.get(1).toString())) {
				series.setType("line");
				series.setSmooth(true);
			}
			if ("2".equals(axiosType.get(1).toString())) {
				series.setType("bar");
				series.setSmooth(true);
				series.setStack("one");
				series.setAreaStyle("{normal: {}}");
			}
			series.setyAxisIndex(1);
			seriesList.add(series);
		
		}
		
		if(measures2.size()>0){
			yAxis = new YAxis();
			String name = measures2.get(0).getFieldNameNew();
			yAxis.setName(name);
			yAxis.setSplitLine("{show: false}");
			yAxies.add(yAxis);
		}
		
		
		// 封装结果数据
		ResultData resultData = new ResultData();
		resultData.setNames(names);		
		resultData.setyAxies(yAxies);
		resultData.setSeries(seriesList);
		resultData.setLegend(legend);
		resultData.setEchartType(echartType);
		return resultData;
	}
	
}
