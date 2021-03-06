package com.bizwell.echarts.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.bean.vo.ResultData;
import com.bizwell.echarts.common.JsonUtils;
import com.bizwell.echarts.exception.EchartsException;
import com.bizwell.echarts.exception.ResponseCode;
import com.bizwell.echarts.service.AbstractReportService;
import com.bizwell.echarts.service.SheetMetaDataService;

/**
 * @author zhangjianjun
 * @date 2018年6月12日
 *
 */
// 计量图模板,用于封装数据
@Service("03Service")
public class GaugeServiceImpl extends AbstractReportService {
	@Autowired
	private SheetMetaDataService sheetMetaDataService;

	@Override
	protected ResultData setupData(List<Map<String, Object>> list, String data, Integer userId) {

		ResultData resultData = new ResultData();
		
		// 获取数值字段
		//List<SheetMetaData> measures = JsonUtils.getFields(data, "measure1", "metadataId", userId);
		List<SheetMetaData> measures = sheetMetaDataService.getFields(data, "measure1", "metadataId");
		
		String echartType = JsonUtils.getString(data, "echartType");
		
		// 只支持一个数值
		if(measures.size() == 1) {
			SheetMetaData sheetMetaData = measures.get(0);
			Object value = new Object();
			for (Map<String, Object> map : list) {
				for(String key :map.keySet()){
					String[] split = key.split("__");
					if(key.startsWith("M") && sheetMetaData.getFieldColumn().equals(split[1])){
						value=(map.get(key));break;
					}
				}
			}
			resultData.setValue(value);
			resultData.setEchartType(echartType);
		} else {
			throw new EchartsException(ResponseCode.ECHARTS_FAIL08.getCode(), ResponseCode.ECHARTS_FAIL08.getMessage());
		}
		return resultData;
	}
	
}
