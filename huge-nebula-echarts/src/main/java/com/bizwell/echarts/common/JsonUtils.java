package com.bizwell.echarts.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bizwell.echarts.bean.domain.SheetMetaData;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// json工具类
public class JsonUtils {
	/*
	public static List<SheetMetaData> getFields(String data, String field, String metadataId, Integer userId) {
		
		List<SheetMetaData> list = new ArrayList<SheetMetaData>();
		
		JSONObject jsonObject = JSONObject.parseObject(data);
		JSONArray jsonArray = jsonObject.getJSONArray(field);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject object = jsonArray.getJSONObject(i);
			Integer id = object.getIntValue(metadataId);
			String tableName = object.getString("tableName");
			SheetMetaData sheetMetadata = MetaDataMap.get(userId, id);
			list.add(sheetMetadata);
		}
		
		return list;
	}*/
	
	// 解析json, 获取字符串类型数据
	public static String getString(String data, String name) {
		if (StringUtils.isEmpty(data) || StringUtils.isEmpty(name)) {
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(data);
		String string = jsonObject.getString(name);
		return string;
	}
	
	// 解析json, 获取字符串类型数据
	public static JSONArray getJSONArray(String data, String name) {
		if (StringUtils.isEmpty(data) || StringUtils.isEmpty(name)) {
			return null;
		}
		JSONObject jsonObject = JSONObject.parseObject(data);
		JSONArray string = jsonObject.getJSONArray(name);
		return string;
	}
	

	// 解析json, 获取字符串类型数据
	public static Integer getInteger(String data, String name) {
		
		JSONObject jsonObject = JSONObject.parseObject(data);
		Integer integer = jsonObject.getInteger(name);
		return integer;
	}
	
	
	/*public static void main(String[] args) {
		
		String jsonString = "{" +
	            "\"echartType\": 2," +
	            "\"dimension\": [{\"metadataId\": 804,\"dateLevel\": \"按周\"}]," +
	            "\"measure1\": [{\"metadataId\": 810,\"aggregate\": \"求和\"}, " +
	            "{\"metadataId\": 809,\"aggregate\": \"计数\"}]," +
	            "\"measure2\": []," +
	            "\"filter\": []}";
		
		JSONObject jsonObject = JSONObject.parseObject(jsonString);
		jsonObject.put("aaa", "新加的");
		jsonObject.put("bbb", new Object());
		String string = jsonObject.getString("");
		System.out.println(jsonObject);
		System.out.println(JSONObject.toJSON(jsonObject));
		Object json = JSONObject.toJSON(jsonObject);
		
			{"x":0,"y":0,"w":4,"h":2},   0
			{"x":4,"y":0,"w":4,"h":2},   1
			{"x":0,"y":2,"w":4,"h":2},   2
			{"x":4,"y":2,"w":4,"h":2},   3
			{"x":0,"y":4,"w":4,"h":2},   4
			{"x":4,"y":4,"w":4,"h":2},   5
		
		Integer[] arr = {0, 1, 2, 3, 4, 5, 6, 7};
		for (int i = 0; i < arr.length; i++) {
			Integer y = (i/2) * 2;
			System.out.println(y);
//			Integer a = i/2;
//			int b = a * 2;
//			System.out.println(b);
			
			Integer x;
			if (i % 2 == 0) {
				x = 0;
			} else {
				x = 4;
			}
			System.out.println("x = " + x);
			StringBuffer sb = new StringBuffer();
			sb.append("{'x':");
			sb.append(x);
			sb.append(",'y':");
			sb.append(y);
			sb.append(",'w':");
			sb.append(4);
			sb.append(",'h':");
			sb.append(2);
			sb.append("}");
			System.out.println(sb);
		}
	}*/
	
}
