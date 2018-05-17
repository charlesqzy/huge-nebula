package com.bizwell.echarts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bizwell.echarts.bean.domain.FolderInfo;
import com.bizwell.echarts.bean.dto.FolderParam;

/**
 * @author zhangjianjun
 * @date 2018年5月14日
 *
 */
public interface FolderInfoMapper {
    
	int deleteByPrimaryKey(Integer id);

    int insert(FolderInfo record);

    int insertSelective(FolderInfo record);

    FolderInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FolderInfo record);

    int updateByPrimaryKey(FolderInfo record);
    
    public List<FolderInfo> selectFolder(@Param("userId") Integer userId);
    
    public List<FolderInfo> selectByParam(FolderParam folderParam);
    
}