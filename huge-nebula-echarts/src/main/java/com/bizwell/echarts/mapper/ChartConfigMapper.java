package com.bizwell.echarts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bizwell.echarts.bean.domain.ChartConfig;

/**
 * @author zhangjianjun
 * @date 2018年5月24日
 *
 */
public interface ChartConfigMapper {
	
    int deleteByPrimaryKey(Integer id);

    int insert(ChartConfig record);

    int insertSelective(ChartConfig record);

    ChartConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ChartConfig record);

    int updateByPrimaryKey(ChartConfig record);
    
    public List<ChartConfig> selectChartConfig(@Param("panelId") Integer panelId,@Param("panelUuid") String panelUuid);
    
}