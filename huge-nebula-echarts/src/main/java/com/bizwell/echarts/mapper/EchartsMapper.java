package com.bizwell.echarts.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * @author zhangjianjun
 * @date 2018年6月22日
 *
 */
// 执行自定义sql
public interface EchartsMapper {
	
	public List<Map<String, Object>> selectBySql(@Param("sql")String sql);
	
	public Integer selectCntBySql(@Param("sql")String sql);

}
