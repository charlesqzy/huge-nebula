package com.bizwell.echarts.common;

import java.util.HashMap;
import java.util.Map;

import com.bizwell.echarts.context.SpringContext;
import com.bizwell.echarts.service.ReportService;

/**
 * AnlsReport.java
 * @date 2015-12-7
 * @author liuyang
 * @version 
 * 
 */
public class ReportManager {
	
	public static final String BAR_LINE = "04";

	private static final Map<String,String> REPORT_NAME = new HashMap<String,String>() {
		
		private static final long serialVersionUID = -6295982379831142005L;

		{
			put(BAR_LINE, "柱形图与折线图");
		}
	};
	
	public static boolean isSupport(String code) {
		return REPORT_NAME.containsKey(code) ? true : false; 
	}
	
	// 获取对应的bean
	public static ReportService getService(String code) {
		return (ReportService) SpringContext.getBean(String.format("%sService", code));
	}
}
