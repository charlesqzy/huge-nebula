package com.bizwell.datasource.mapper;


import java.util.List;

import com.bizwell.datasource.bean.FolderInfo;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface FolderInfoMapper {

	Integer save(FolderInfo entity);
    
    List<FolderInfo> select(FolderInfo entity);
    
}