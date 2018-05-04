package com.bizwell.datasource.mapper;

import java.util.List;

import com.bizwell.datasource.bean.ExcelSheetInfo;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface ExcelSheetInfoMapper {

	Integer save(ExcelSheetInfo entity);
    
    List<ExcelSheetInfo> select(ExcelSheetInfo entity);
    
    
}