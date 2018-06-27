package com.bizwell.passport.common;

import com.google.gson.Gson;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// json工具类
public class JsonUtils {

	public static String toJson(Object obj) {
		return new Gson().toJson(obj);
	}
}
