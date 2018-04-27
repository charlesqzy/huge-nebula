package com.bizwell.datasource.exception;

/**
 * @author zhangjianjun
 * @date 2018年2月2日
 *
 */
public class PassportException extends RuntimeException {

	private static final long serialVersionUID = -5929924385474786461L;
	
	protected int code;

    protected String message;

	public PassportException(int code, String message) {
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
