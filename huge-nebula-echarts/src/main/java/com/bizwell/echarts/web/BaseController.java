package com.bizwell.echarts.web;

import com.bizwell.echarts.common.JsonView;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
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
