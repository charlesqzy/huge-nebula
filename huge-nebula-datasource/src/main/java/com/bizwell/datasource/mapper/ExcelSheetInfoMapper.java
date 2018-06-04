package com.bizwell.datasource.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bizwell.datasource.bean.ExcelSheetInfo;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface ExcelSheetInfoMapper {

	Integer save(ExcelSheetInfo entity);
    
    List<ExcelSheetInfo> select(ExcelSheetInfo entity);
    
    Integer update(ExcelSheetInfo entity);    
    
    List<Map<String,String>> getSheetDataByTableName(@Param("tableName") String tableName,@Param("start") Integer start,@Param("end") Integer end);
    
    Integer getCountByTableName(@Param("tableName") String tableName);
    
    void delete(ExcelSheetInfo entity);
}