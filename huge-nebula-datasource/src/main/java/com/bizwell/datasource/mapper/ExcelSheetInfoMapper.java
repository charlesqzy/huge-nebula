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
    
    
    List<Map> getSheetDataByTableName(@Param("tableName") String tableName);
    
    void delete(ExcelSheetInfo entity);
}