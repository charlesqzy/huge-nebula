package com.bizwell.echarts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bizwell.echarts.bean.domain.SheetMetaData;

/**
 * @author zhangjianjun
 * @date 2018年5月21日
 *
 */
public interface SheetMetaDataMapper {
   
	int deleteByPrimaryKey(Integer id);

    int insert(SheetMetaData record);

    int insertSelective(SheetMetaData record);

    SheetMetaData selectByPrimaryKey(Integer id);
    
    List<SheetMetaData> selectByIds(@Param("ids")List<Integer> ids);

    int updateByPrimaryKeySelective(SheetMetaData record);

    int updateByPrimaryKey(SheetMetaData record);
    
    public List<SheetMetaData> selectByUserId(@Param("userId") Integer userId);
    
}