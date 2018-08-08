package com.bizwell.datasource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.MysqlTableConf;
import com.bizwell.datasource.mapper.MysqlTableConfMapper;

@Service
public class MysqlTableConfService {
	
	@Autowired
	private MysqlTableConfMapper mysqlTableConfMapper;
	
	
	public void save(MysqlTableConf entity){
		mysqlTableConfMapper.save(entity);
	}
	
	
	public void save(List<MysqlTableConf> list){
		mysqlTableConfMapper.saveBatch(list);
	}
	
	public List<MysqlTableConf> select(MysqlTableConf entity){
		return mysqlTableConfMapper.select(entity);
	}
}
