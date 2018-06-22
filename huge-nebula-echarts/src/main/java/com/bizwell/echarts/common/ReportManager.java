package com.bizwell.echarts.common;

import java.util.HashMap;
import java.util.Map;

import com.bizwell.echarts.context.SpringContext;
import com.bizwell.echarts.service.ReportService;

/**
 * @author zhangjianjun
 * @date 2018年6月22日
 *
 */
// 系统管理类
public class ReportManager {
	
	// 表格
	public static final String FORM = "01";
	
	// 指标卡
	public static final String CARD = "02";
	
	// 计量图
	public static final String GAUGE = "03";
	
	// 柱形图与折线图
	public static final String BAR_LINE = "04";
	
	// 饼图
	public static final String PIE = "05";

	// 设置code对应中文
	private static final Map<String,String> REPORT_NAME = new HashMap<String,String>() {
		
		private static final long serialVersionUID = -6295982379831142005L;

		{
			put(FORM, "表格");
			put(CARD, "指示卡");
			put(GAUGE, "仪表盘");
			put(BAR_LINE, "柱形图与折线图");
			put(PIE, "饼图");
		}
	};
	
	// 通过code判断是否是系统支持的
	public static boolean isSupport(String code) {
		return REPORT_NAME.containsKey(code) ? true : false; 
	}
	
	// 获取对应的bean
	public static ReportService getService(String code) {
		return (ReportService) SpringContext.getBean(String.format("%sService", code));
	}
}
