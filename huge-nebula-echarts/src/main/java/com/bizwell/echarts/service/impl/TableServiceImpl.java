package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.FormHeader;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.common.ReportManager;
import com.bizwell.echarts.service.FormService;
import com.bizwell.echarts.service.ReportService;
import com.bizwell.echarts.service.SheetMetaDataService;
import com.github.pagehelper.PageHelper;

/**
 * @author zhangjianjun
 * @date 2018年6月21日
 *
 */
// 表格模板,仅在仪表盘加载数据时调用一次,用于查询第一页数据
@Service("01Service")
public class TableServiceImpl implements ReportService {
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private SheetMetaDataService sheetMetaDataService;
	
	@Override
	public ResultData selectEcharts(String data, Integer userId) {

		ResultData resultData = new ResultData();
		String echartType = JsonUtils.getString(data, "echartType");
		

		List<FormHeader> headerList = new ArrayList<FormHeader>();
		// 获取用户选中的所有维度与数值,用于组装成表头
		List<SheetMetaData> list2 = getMetaData(data);
		if (list2.size() >= 1) {
			// 将数值组装进表头
			getHeader(headerList, list2);
		}
		
		// 分页查询出表格数据
		List<Map<String,Object>> list = formService.selectList(data,0,30);//取出头30条
		// 查询出数据的总条数
		Integer cnt = formService.selectCnt(data);
		
		
		
		List<FormHeader> newHeaderList = getNewHaderList(list, headerList);
		
		// 将表头,数据,总条数封装进map中
		Map<String, Object> map = new HashMap<>();
		map.put("header", newHeaderList);
		map.put("list", list);
		map.put("total", cnt);
		
		resultData.setValue(map);
		resultData.setEchartType(echartType);
		return resultData;
	}
	
	

	private List<FormHeader> getNewHaderList(List<Map<String, Object>> list, List<FormHeader> headerList) {
		List<FormHeader> newHeaderList = new ArrayList<FormHeader>();
		
		
		if(list.size()>0){
			 for(String key :list.get(0).keySet()){
//				 if(key.endsWith("D")){
					 String column = key.split("__")[1];
					 String aggregate = ReportManager.getAggregate(key);
					 String label="";
					 for(FormHeader header : headerList){
						 if(column.equals(header.getProp())){
							 label= header.getLabel();break;
						 }
					 }
					 
					 FormHeader header = new FormHeader();
					 header.setLabel(label+aggregate);
					 header.setProp(key);
					 newHeaderList.add(header);
//				 }
			 }
		}
		return newHeaderList;
	}

	
	// 获取用户选中的所有维度与数值,用于组装成表头
	private List<SheetMetaData> getMetaData(String data) {
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		// 获取维度字段名称
		List<SheetMetaData> dimensions = sheetMetaDataService.getFields(data, "dimension", "metadataId");
		for (SheetMetaData sheetMetaData : dimensions) {
			list.add(sheetMetaData);
		}
		
		// 获取数值字段
		List<SheetMetaData> measures = sheetMetaDataService.getFields(data, "measure1", "metadataId");
		for (SheetMetaData sheetMetaData : measures) {
			list.add(sheetMetaData);
		}
		return list;
	}
	
	// 将数值组装进表头
	private void getHeader(List<FormHeader> headerList, List<SheetMetaData> list) {
		
		for (SheetMetaData sheetMetaData : list) {
			FormHeader formHeader = new FormHeader();
			formHeader.setProp(sheetMetaData.getFieldColumn());
			formHeader.setLabel(sheetMetaData.getFieldNameNew());
			headerList.add(formHeader);
		}
	}

}
