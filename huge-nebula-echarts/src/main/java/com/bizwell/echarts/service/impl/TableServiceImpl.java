package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.FormHeader;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.service.FormService;
import com.bizwell.echarts.service.ReportService;
import com.github.pagehelper.PageHelper;

/**
 * @author zhangjianjun
 * @date 2018年6月21日
 *
 */
@Service("01Service")
public class TableServiceImpl implements ReportService {
	
	@Autowired
	private FormService formService;
	
	@Override
	public ResultData selectEcharts(String data, Integer userId) {

		ResultData resultData = new ResultData();
		String echartType = JsonUtils.getString(data, "echartType");
		
		PageHelper.startPage(1, 30);
		List<FormHeader> headerList = new ArrayList<FormHeader>();
		List<SheetMetaData> list2 = getMetaData(data, userId);
		if (list2.size() >= 1) {
			getHeader(headerList, list2);
		}
		
		List<Map<String,Object>> list = formService.selectList(data, userId);
		Integer cnt = formService.selectCnt(data, userId);
		
		Map<String, Object> map = new HashMap<>();
		map.put("header", headerList);
		map.put("list", list);
		map.put("total", cnt);
		
		resultData.setValue(map);
		resultData.setEchartType(echartType);
		return resultData;
	}
	
	
	private List<SheetMetaData> getMetaData(String data, Integer userId) {
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		// 获取维度字段名称
		List<SheetMetaData> dimensions = JsonUtils.getFields(data, "dimension", "metadataId", userId);
		for (SheetMetaData sheetMetaData : dimensions) {
			list.add(sheetMetaData);
		}
		
		// 获取数值字段
		List<SheetMetaData> measures = JsonUtils.getFields(data, "measure1", "metadataId", userId);
		for (SheetMetaData sheetMetaData : measures) {
			list.add(sheetMetaData);
		}
		return list;
	}
	
	private void getHeader(List<FormHeader> headerList, List<SheetMetaData> list) {
		
		for (SheetMetaData sheetMetaData : list) {
			FormHeader formHeader = new FormHeader();
			formHeader.setProp(sheetMetaData.getFieldColumn());
			formHeader.setLabel(sheetMetaData.getFieldNameNew());
			headerList.add(formHeader);
		}
	}

}
