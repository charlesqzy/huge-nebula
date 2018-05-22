package com.bizwell.echarts.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.mapper.SheetMetaDataMapper;
import com.bizwell.echarts.service.SheetMetaDataService;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
@Service
public class SheetMetaDataServiceImpl implements SheetMetaDataService {

	@Autowired
	private SheetMetaDataMapper sheetMetaDataMapper;
	
	@Override
	public Map<Integer, SheetMetaData> loadProperty() {
		
		Map<Integer, SheetMetaData> map = new HashMap<Integer, SheetMetaData>();
		List<SheetMetaData> list = sheetMetaDataMapper.selectAll();
		for (SheetMetaData sheetMetaData : list) {
			map.put(sheetMetaData.getId(), sheetMetaData);
		}
		return map;
	}

}
