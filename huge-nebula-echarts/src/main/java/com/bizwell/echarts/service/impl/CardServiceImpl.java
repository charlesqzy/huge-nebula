package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.PieData;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.AbstractReportService;


/**
 * @author zhangjianjun
 * @date 2018年6月12日
 *
 */
// 指标卡模板,用于封装数据
@Service("02Service")
public class CardServiceImpl extends AbstractReportService {

	@Override
	protected ResultData setupData(List<Map<String, Object>> list, String data, Integer userId) {

		ResultData resultData = new ResultData();
		// 获取数值字段
		List<SheetMetaData> measures = JsonUtils.getFields(data, "measure1", "metadataId", userId);
		
		List<Object> dataList = new ArrayList<Object>();
		String echartType = JsonUtils.getString(data, "echartType");
		
		int size = measures.size();
		// 没有维度, 一个或两个数值
		if(size >= 1 && size <= 2) {
			
			
			for (SheetMetaData sheetMetaData : measures) {
				Object value = new Object();
				for (Map<String, Object> map : list) {
					//value = map.get(sheetMetaData.getFieldColumn());
					for(String key :map.keySet()){
						if(key.endsWith("M")){
							value=(map.get(key));
						}
					}
				}
				String name = sheetMetaData.getFieldNameNew();
				PieData pieData = getPieData(name, value);
				dataList.add(pieData);
			}
			
			
			resultData.setNames(dataList);
			resultData.setEchartType(echartType);
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
	
}
