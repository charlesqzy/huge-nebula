package com.bizwell.datasource.mapper;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bizwell.datasource.bean.SheetMetadata;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface SheetMetadataMapper {

	Integer save(SheetMetadata entity);
    
    List<SheetMetadata> select(SheetMetadata entity);
    
    List<Map> getXlsDataByFilter(@Param("tableName") String tableName,@Param("fieldColumn") String fieldColumn);
}