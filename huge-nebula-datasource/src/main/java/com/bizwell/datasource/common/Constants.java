package com.bizwell.datasource.common;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public interface Constants {
	
	/** AES加密算法KEY */
	public static final String AES_KEY = "41E8383956CBE4C7FAEE7B98460BE60E";
	
	/** 生成的cookie名 */
//	public final static String TICKET_COOKIE_NAME = "passport_ticket";
	
	/** ticket前缀 */
	public final static String STORED_PREFIX = "passport_ticket:";
	
	/** ticket失效时间 */
	public final static Long TICKET_EXPIRED_TIME = (long)1800;
//	public final static Long TICKET_EXPIRED_TIME = (long)60;
	
	/** mapper路径 */
	public static final String MAPPER_PATH = "com.bizwell.datasource.mapper";
	
	/** 实体类路径 */
	public static final String DO_MAIN_PATH ="com.bizwell.datasource.bean";
	
	/** mapper xml 路径 */
	public static final String MAPPER_XML_PATH = "classpath:com/bizwell/datasource/mapper/*.xml";
	
	
	public static String excelHader[] = { "A", "B", "C", "D", "E", "F", "G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
			"AA", "AB", "AC", "AD", "AE", "AF", "AG","AH","AI","AJ","AK","AL","AM","AN","AO","AP","AQ","AR","AS","AT","AU","AV","AW","AX","AY","AZ"};
	
	
}
