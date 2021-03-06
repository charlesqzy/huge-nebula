package com.bizwell.echarts.common;

import java.io.Serializable;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 封装后台返回给前台的数据模板类
public class JsonView implements Serializable {

	private static final long serialVersionUID = 8503867829119469537L;

	private Integer code;

	private String message;
	
	private Object data;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return String.format("JsonView [code=%s, message=%s, data=%s]", code, message, data);
	}

}
