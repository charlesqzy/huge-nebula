package com.bizwell.datasource.common;

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
}
