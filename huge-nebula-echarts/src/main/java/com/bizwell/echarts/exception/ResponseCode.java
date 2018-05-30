package com.bizwell.echarts.exception;

/**
 * @author zhangjianjun
 * @date 2018年2月2日
 * 错误信息
 *
 */
public enum ResponseCode {
	
	SUCCESS(1000, "成功"),
	ERROR(9999, "失败"),
	
	ECHARTS_FAIL01(5001, "用户Id为空"),
	ECHARTS_FAIL02(5002, "文件Id为空"),
	ECHARTS_FAIL03(5003, "文件父Id为空"),
	ECHARTS_FAIL04(5004, "文件名称为空"),
	ECHARTS_FAIL05(5005, "文件类型为空"),
	ECHARTS_FAIL06(5006, "不支持的图表"),
	ECHARTS_FAIL07(5007, "仪表盘id为空");
	
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
