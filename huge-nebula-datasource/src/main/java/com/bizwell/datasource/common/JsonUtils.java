package com.bizwell.datasource.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public class JsonUtils {

	public static String toJson(Object obj) {
		//return new Gson().toJson(obj);
		 Gson gson = new GsonBuilder()
		.setDateFormat("yyyy-MM-dd HH:mm:ss")
        .create();
		return gson.toJson(obj);
	}
}
