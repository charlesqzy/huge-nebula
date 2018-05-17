package com.bizwell.echarts.service;

import com.bizwell.echarts.bean.dto.QueryParam;

/**
 * @author zhangjianjun
 * @date 2018年4月28日
 *
 */
public interface JDBCService {
	
	public void getUser(QueryParam param) throws Exception;

}
