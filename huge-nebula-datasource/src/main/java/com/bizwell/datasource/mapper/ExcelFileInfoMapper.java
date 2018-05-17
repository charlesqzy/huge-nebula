package com.bizwell.datasource.mapper;


import java.util.List;

import com.bizwell.datasource.bean.ExcelFileInfo;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface ExcelFileInfoMapper {

	Integer save(ExcelFileInfo entity);
    
    List<ExcelFileInfo> select(ExcelFileInfo entity);
    
}