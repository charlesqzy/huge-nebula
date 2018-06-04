package com.bizwell.datasource.common;

import java.text.DateFormat;
import java.text.ParseException;
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
	
	
	   /**
	    * 判断是否是对应的格式的日期字符串
	    * @param dateStr
	    * @param datePattern
	    * @return
	    */
	   public static boolean isRightDateStr(String dateStr,String datePattern){
	       DateFormat dateFormat  = new SimpleDateFormat(datePattern);
	       try {
	           //采用严格的解析方式，防止类似 “2017-05-35” 类型的字符串通过
	           dateFormat.setLenient(false);
	           dateFormat.parse(dateStr);
	           Date date = (Date)dateFormat.parse(dateStr);
	           //重复比对一下，防止类似 “2017-5-15” 类型的字符串通过
	           String newDateStr = dateFormat.format(date);
	           if(dateStr.equals(newDateStr)){
	               return true;
	           }else {
	               //logger.error("字符串dateStr:{}， 不是严格的 datePattern:{} 格式的字符串",dateStr,datePattern);
	               return false;
	           }
	       } catch (ParseException e) {
	    	   //logger.error("字符串dateStr:{}，不能按照 datePattern:{} 样式转换",dateStr,datePattern);
	           return false;
	       }
	   }
	   
		
	
}
