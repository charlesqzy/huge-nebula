package com.bizwell.datasource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.MysqlConnConf;
import com.bizwell.datasource.mapper.MysqlConnConfMapper;

@Service
public class MysqlConnConfService {
	
	@Autowired
	private MysqlConnConfMapper mysqlConnConfMapper;
	
	
	public Integer save(MysqlConnConf entity){
		return mysqlConnConfMapper.save(entity);
	}
	
	
	public List<MysqlConnConf> select(MysqlConnConf entity){
		return mysqlConnConfMapper.select(entity);
	}
}
