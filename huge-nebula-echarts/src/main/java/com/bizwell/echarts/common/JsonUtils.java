package com.bizwell.echarts.common;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;
import com.google.gson.Gson;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public class JsonUtils {

	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}
	
	
	public static List<SheetMetaData> getFields(String data, String field, String metadataId) {
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		
		JSONObject jsonObject = JSONObject.parseObject(data);
		JSONArray jsonArray = jsonObject.getJSONArray(field);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			Integer id = object.getIntValue(metadataId);
			SheetMetaData sheetMetadata = MetaDataMap.get(id);
			list.add(sheetMetadata);
		}
		
		return list;
	}
	
	
}
