package com.bizwell.datasource.web;

import com.bizwell.datasource.common.JsonView;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public class BaseController {
	
	protected JsonView result(Integer code, String message, Object data) {
		
		JsonView jsonView = new JsonView();
		jsonView.setCode(code);
		jsonView.setMessage(message);
		jsonView.setData(data);
		return jsonView;
	}
	
}
