package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.PieData;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.bean.vo.Series;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.ReportManager;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.AbstractReportService;
import com.bizwell.echarts.service.SheetMetaDataService;


/**
 * @author zhangjianjun
 * @date 2018年6月11日
 *
 */
// 饼图模板,用于封装数据
@Service("05Service")
public class PieServiceImpl extends AbstractReportService {
	
	@Autowired
	private SheetMetaDataService sheetMetaDataService;

	@Override
	protected ResultData setupData(List<Map<String, Object>> list, String data, Integer userId) {

		ResultData resultData = new ResultData();
		
		// 获取维度字段名称
		//List<SheetMetaData> dimensions = JsonUtils.getFields(data, "dimension", "metadataId", userId);
		List<SheetMetaData> dimensions = sheetMetaDataService.getFields(data, "dimension", "metadataId");
		// 获取数值字段
		List<SheetMetaData> measures = sheetMetaDataService.getFields(data, "measure1", "metadataId");
		
		List<String> names = new ArrayList<String>();
		List<Object> dataList = new ArrayList<Object>();
		String type = JsonUtils.getString(data, "type");
		String echartType = JsonUtils.getString(data, "echartType");
		
		// 一个维度, 一个数值
		if (dimensions.size() == 1 && measures.size() == 1) {
			for (Map<String, Object> map : list) {
				
				String name="";
				Object value=null;
				
				for(String key :map.keySet()){
					if(key.startsWith("D")){
						String aggregate = ReportManager.getAggregate(key);
						name=((String)map.get(key))+aggregate;
					}
					if(key.startsWith("M")){
						value=(map.get(key));
					}
				}
				
				PieData pieData = getPieData(name, value);
				names.add(name);
				dataList.add(pieData);
			}
			resultData = getResultData(type, dataList, names, echartType);
			
		// 没有维度, 多个数值
		} else if(dimensions.size() == 0 && measures.size() >= 1) {
			
			int n=0;
			for (SheetMetaData sheetMetaData : measures) {
				Object value = new Object();
				String aggregate = "";
				for (Map<String, Object> map : list) {
					for(String key :map.keySet()){
						String[] split = key.split("__");
						if(key.startsWith("M1"+String.format("%02d", n)) && sheetMetaData.getFieldColumn().equals(split[1])){
							value = map.get(key);
							aggregate = ReportManager.getAggregate(key);
						}
					}
				}
				n++;
				
				String name = sheetMetaData.getFieldNameNew()+aggregate;
				PieData pieData = getPieData(name, value);
				names.add(name);
				dataList.add(pieData);
			}
			resultData = getResultData(type, dataList, names, echartType);
		} else {
			throw new EchartsException(ResponseCode.ECHARTS_FAIL08.getCode(), ResponseCode.ECHARTS_FAIL08.getMessage());
		}
		return resultData;
	}
	
	private PieData getPieData(String name, Object value) {
		
		PieData pieData = new PieData();
		pieData.setName(name);
		pieData.setValue(value);
		return pieData;
	}
	
	private ResultData getResultData(String type, List<Object> dataList, List<String> names, String echartType) {
		
		List<Series> seriesList = new ArrayList<Series>();
		Series series = new Series();
		series.setType(type);
		series.setData(dataList);
		seriesList.add(series);
		
		ResultData resultData = new ResultData();
		resultData.setSeries(seriesList);
		resultData.setLegend(names);
		resultData.setEchartType(echartType);
		return resultData;
	}
	
}
