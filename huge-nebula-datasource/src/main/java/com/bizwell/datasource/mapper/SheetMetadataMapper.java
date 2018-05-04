package com.bizwell.datasource.mapper;


import java.util.List;

import com.bizwell.datasource.bean.SheetMetadata;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface SheetMetadataMapper {

	Integer save(SheetMetadata entity);
    
    List<SheetMetadata> select(SheetMetadata entity);
    
}