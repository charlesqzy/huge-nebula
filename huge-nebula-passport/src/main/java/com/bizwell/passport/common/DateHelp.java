package com.bizwell.passport.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public class DateHelp {
	
	public static String getStrDay(Date date) {
		
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String string = sdf.format(date);
		return string;
	}
	
	public static String getStrMonth(Date date) {
		
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		String string = sdf.format(date);
		return string;
	}
	
	public static String getStrTime(Date date) {
		
		if (date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String string = sdf.format(date);
		return string;
	}
	
	public static Date getYesterday() {
		Date date=new Date();  
		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);  
		calendar.add(Calendar.DAY_OF_MONTH, -1);  
		date = calendar.getTime();
		return date;
	}
	
}
