package com.bizwell.echarts.service;

import java.util.List;
import java.util.Map;

/**
 * @author zhangjianjun
 * @date 2018年6月13日
 *
 */
public interface FormService {
	
	public List<Map<String,Object>> selectList(String data,int start,int end);
	
	public Integer selectCnt(String data);

}
