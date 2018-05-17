package com.bizwell.echarts.web.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class FsonTest {
	
	public static void main(String[] args) {
		
		//json字符串-简单对象型
		String  JSON_OBJ_STR = "{\"studentName\":\"lily\",\"studentAge\":12}";

		//json字符串-数组类型
		String  JSON_ARRAY_STR = "[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]";

		//复杂格式json字符串
		String  COMPLEX_JSON_STR = "{\"teacherName\":\"crystall\",\"teacherAge\":27,\"course\":{\"courseName\":\"english\",\"code\":1270},\"students\":[{\"studentName\":\"lily\",\"studentAge\":12},{\"studentName\":\"lucy\",\"studentAge\":15}]}";
		
//		JSONObject jsonObject = JSONObject.parseObject(JSON_OBJ_STR);

//	    System.out.println("studentName:  " + jsonObject.getString("studentName") + ":" + "  studentAge:  "
//	            + jsonObject.getInteger("studentAge"));
		
	    // 第一种方式
//	    String jsonString = JSONObject.toJSONString(jsonObject);

	    // 第二种方式
	    //String jsonString = jsonObject.toJSONString();
//	    System.out.println(jsonString);
	    
	    JSONArray jsonArray = JSONArray.parseArray(JSON_ARRAY_STR);

	    //遍历方式1
	    int size = jsonArray.size();
	    for (int i = 0; i < size; i++) {

	        JSONObject jsonObject = jsonArray.getJSONObject(i);
	        System.out.println("studentName:  " + jsonObject.getString("studentName") + ":" + "  studentAge:  "
	                + jsonObject.getInteger("studentAge"));
	    }

	    //遍历方式2
	    for (Object obj : jsonArray) {

	        JSONObject jsonObject = (JSONObject) obj;
	        System.out.println("studentName:  " + jsonObject.getString("studentName") + ":" + "  studentAge:  "
	                + jsonObject.getInteger("studentAge"));
	    }
	    
		
	}

}
