package com.bizwell.passport.common;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 配置常量信息
public interface Constants {
	
	/** AES加密算法KEY */
	public static final String AES_KEY = "41E8383956CBE4C7FAEE7B98460BE60E";
	
	/** 生成的cookie名 */
//	public final static String TICKET_COOKIE_NAME = "passport_ticket";
	
	/** ticket前缀 */
	public final static String STORED_PREFIX = "passport_ticket:";
	
	/** ticket失效时间(单位秒) */
	// 半小时,正常时可用
//	public final static Long TICKET_EXPIRED_TIME = (long)1800;
	// 调长时间,方便黄越测试用(12小时)
	public final static Long TICKET_EXPIRED_TIME = (long)43200;
	// 1分钟
//	public final static Long TICKET_EXPIRED_TIME = (long)60;
	
	/** mapper路径 */
	public static final String MAPPER_PATH = "com.bizwell.passport.mapper";
	
	/** 实体类路径 */
	public static final String DO_MAIN_PATH ="com.bizwell.passport.bean.domain";
	
	/** mapper xml 路径 */
	public static final String MAPPER_XML_PATH = "classpath:com/bizwell/passport/mapper/*.xml";
	
}
