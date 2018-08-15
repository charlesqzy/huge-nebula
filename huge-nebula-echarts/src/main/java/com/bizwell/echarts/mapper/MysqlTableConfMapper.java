package com.bizwell.echarts.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bizwell.echarts.bean.MysqlTableConf;


/**
 *
 * Created by liujian on 2017/10/9.
 */
public interface MysqlTableConfMapper {

	void save(MysqlTableConf entity);

	List<MysqlTableConf> select(MysqlTableConf entity);

	void saveBatch(List<MysqlTableConf> list);

	void deleteByConnId(@Param("ids") List<Integer> ids);

	List<MysqlTableConf> selectDatabase(MysqlTableConf entity);

}