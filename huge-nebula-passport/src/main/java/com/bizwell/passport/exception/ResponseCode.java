package com.bizwell.passport.exception;

/**
 * @author zhangjianjun
 * @date 2018年2月2日
 * 错误信息
 *
 */
// 异常信息
public enum ResponseCode {
	
	SUCCESS(1000, "成功"),
	ERROR(9999, "失败"),
	
	PASSPORT_FAIL01(5001, "用户名为空"),
	PASSPORT_FAIL02(5002, "密码为空"),
	PASSPORT_FAIL03(5003, "该用户不存在"),
	PASSPORT_FAIL04(5004, "密码错误"),
	PASSPORT_FAIL05(5005, "ticket生成失败"),
	PASSPORT_FAIL06(5006, "ticket获取失败"),
	PASSPORT_FAIL07(5007, "ticket删除失败"),
	PASSPORT_FAIL08(5008, "该用户名已经注册"),
	PASSPORT_FAIL09(5009, "公司名称为空"),
	PASSPORT_FAIL10(5010, "ticket为空");
	
	// 编码
    private Integer code;
    // 描述
    private String message;
    
	private ResponseCode() {
	
	}

	private ResponseCode(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

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
    
}
