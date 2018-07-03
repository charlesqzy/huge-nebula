package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.bizwell.echarts.common.MetaDataMap;
import com.bizwell.echarts.mapper.SheetMetaDataMapper;
import com.bizwell.echarts.service.SheetMetaDataService;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
// 表格源数据service
@Service
public class SheetMetaDataServiceImpl implements SheetMetaDataService {

	@Autowired
	private SheetMetaDataMapper sheetMetaDataMapper;
	
	
	public List<SheetMetaData> getFields(String data, String field, String metadataId) {
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		
		JSONObject jsonObject = JSONObject.parseObject(data);
		JSONArray jsonArray = jsonObject.getJSONArray(field);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			Integer id = object.getIntValue(metadataId);
			//String tableName = object.getString("tableName");
			//SheetMetaData sheetMetadata = MetaDataMap.get(userId, id);
			SheetMetaData sheetMetadata = sheetMetaDataMapper.selectByPrimaryKey(id);
			list.add(sheetMetadata);
		}
		
		return list;
	}
	
	
	// 加载数据
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

	// 刷新数据
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
