package com.bizwell.echarts.exception;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
 *
 */
// 自定义异常类
public class EchartsException extends RuntimeException {

	private static final long serialVersionUID = -3083072543662560696L;

	protected int code;

    protected String message;

	public EchartsException(int code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return String.format("ReportException [code=%s, message=%s]", code, message);
	}

}
