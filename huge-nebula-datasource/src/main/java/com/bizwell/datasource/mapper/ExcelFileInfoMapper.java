package com.bizwell.datasource.mapper;


import com.bizwell.datasource.bean.ExcelFileInfo;

import org.apache.ibatis.annotations.Param;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface ExcelFileInfoMapper {

    void save(ExcelFileInfo entity);
}