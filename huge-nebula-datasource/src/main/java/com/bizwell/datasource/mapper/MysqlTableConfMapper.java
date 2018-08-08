package com.bizwell.datasource.mapper;


import java.util.List;

import com.bizwell.datasource.bean.MysqlTableConf;

/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface MysqlTableConfMapper {

	void save(MysqlTableConf entity);
    
    List<MysqlTableConf> select(MysqlTableConf entity);

	void saveBatch(List<MysqlTableConf> list);
    
}