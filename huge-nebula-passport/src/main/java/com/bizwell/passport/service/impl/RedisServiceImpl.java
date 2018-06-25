package com.bizwell.passport.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.bizwell.passport.service.RedisService;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Service
public class RedisServiceImpl implements RedisService {
	
	@Autowired
	private ValueOperations<String, Object> valueOperations;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
	// 获取数据
	@Override
	public Object getValue(String key) {
		
		Object value = valueOperations.get(key);
		return value;
	}

	// 保存数据
	@Override
	public void setValue(String key, Object value) {
		
		valueOperations.set(key, value);
	}

	// 保存数据,设置超时时间
	@Override
	public void setValue(String key, Object value, Long expiredTime) {

		valueOperations.set(key, value, expiredTime, TimeUnit.SECONDS);
	}

	// 删除数据
	@Override
	public void removeKey(String key) {
		
		redisTemplate.delete(key);
	}
	
}
