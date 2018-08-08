package com.bizwell.datasource.mapper;


import java.util.List;

import com.bizwell.datasource.bean.MysqlConnConf;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface MysqlConnConfMapper {

	Integer save(MysqlConnConf entity);
    
    List<MysqlConnConf> select(MysqlConnConf entity);
    
}