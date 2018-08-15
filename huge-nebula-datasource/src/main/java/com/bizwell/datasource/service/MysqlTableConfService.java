package com.bizwell.datasource.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.DatabaseInfo;
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
	
	public List<DatabaseInfo> selectDatabase(MysqlTableConf entity){
		
		DatabaseInfo databaseInfo = null;
		List<DatabaseInfo> list = new ArrayList();
		
		
		List<MysqlTableConf> selectDatabase = mysqlTableConfMapper.selectDatabase(entity);
		for(MysqlTableConf database : selectDatabase){
			String databaseName = database.getDatabaseName();			
			databaseInfo = new DatabaseInfo();			
			databaseInfo.setDatabaseName(databaseName);
			databaseInfo.setConnId(database.getConnId());
			databaseInfo.setConnName(database.getConnName());
			
			entity.setDatabaseName(databaseName);
			entity.setConnId(database.getConnId());
			List<MysqlTableConf> selectTable = mysqlTableConfMapper.select(entity);
			
			for(MysqlTableConf table : selectTable){
				databaseInfo.getTableNames().add(table.getTableName());	
			}
			
			list.add(databaseInfo);
		}
		
		
		return list;
	}
	
	public void deleteByConnId(List<Integer> ids){
		mysqlTableConfMapper.deleteByConnId(ids);
	}
}
