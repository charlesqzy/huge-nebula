package com.bizwell.passport.service;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public interface RedisService {
	
	public Object getValue(String key);
	
	public void setValue(String key, Object value);
	
	public void setValue(String key, Object value, Long expiredTime);
	
	public void removeKey(String key);

}
