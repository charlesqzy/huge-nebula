package com.bizwell.echarts.web;

import com.bizwell.echarts.common.JsonView;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
 *
 */
// 顶层公共controller,用于封装公共方法
public class BaseController {
	
	// 封装返回给前台数据
	protected JsonView result(Integer code, String message, Object data) {
		
		JsonView jsonView = new JsonView();
		jsonView.setCode(code);
		jsonView.setMessage(message);
		jsonView.setData(data);
		return jsonView;
	}
	
}
