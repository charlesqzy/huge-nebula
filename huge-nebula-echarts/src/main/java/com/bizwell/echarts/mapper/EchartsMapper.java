package com.bizwell.echarts.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface EchartsMapper {
	
	public List<Map<String, Object>> selectBySql(@Param("sql")String sql);
	
	public Integer selectCntBySql(@Param("sql")String sql);

}
