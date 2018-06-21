package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.common.MetaDataMap;
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
	public ConcurrentMap<Integer,ConcurrentMap<Integer,SheetMetaData>> loadProperty() {
		
		
		ConcurrentMap<Integer,ConcurrentMap<Integer,SheetMetaData>> resulthMap = new ConcurrentHashMap<Integer, ConcurrentMap<Integer, SheetMetaData>>();
		
		ConcurrentHashMap<Integer, List<SheetMetaData>> map = new ConcurrentHashMap<Integer, List<SheetMetaData>>();
		List<SheetMetaData> list = sheetMetaDataMapper.selectByUserId(null);
		for (SheetMetaData sheetMetaData : list) {
			Integer userId = sheetMetaData.getUserId();
			if (!map.containsKey(userId)) {
				map.put(userId, new ArrayList<SheetMetaData>());
			}
			map.get(userId).add(sheetMetaData);
		}
		
		for (Entry<Integer, List<SheetMetaData>> entry : map.entrySet()) {
			
			ConcurrentHashMap<Integer, SheetMetaData> map1 = new ConcurrentHashMap<Integer, SheetMetaData>();
			List<SheetMetaData> list1 = entry.getValue();
			for (SheetMetaData sheetMetaData : list1) {
				map1.put(sheetMetaData.getId(), sheetMetaData);
			}
			resulthMap.put(entry.getKey(), map1);
		}
		
		return resulthMap;
	}

	@Override
	public void refresh(Integer userId) {

		ConcurrentHashMap<Integer, List<SheetMetaData>> map = new ConcurrentHashMap<Integer, List<SheetMetaData>>();
		List<SheetMetaData> list = sheetMetaDataMapper.selectByUserId(userId);
		for (SheetMetaData sheetMetaData : list) {
			if (!map.containsKey(userId)) {
				map.put(userId, new ArrayList<SheetMetaData>());
			}
			map.get(userId).add(sheetMetaData);
		}
		
		for (Entry<Integer, List<SheetMetaData>> entry : map.entrySet()) {
			
			ConcurrentHashMap<Integer, SheetMetaData> map1 = new ConcurrentHashMap<Integer, SheetMetaData>();
			List<SheetMetaData> list1 = entry.getValue();
			for (SheetMetaData sheetMetaData : list1) {
				map1.put(sheetMetaData.getId(), sheetMetaData);
			}
			MetaDataMap.put(entry.getKey(), map1);
		}
	}
	
}
