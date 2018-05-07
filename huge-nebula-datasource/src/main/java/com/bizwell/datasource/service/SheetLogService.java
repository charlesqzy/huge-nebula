package com.bizwell.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.SheetLog;
import com.bizwell.datasource.mapper.SheetLogMapper;

@Service
public class SheetLogService {
	
	@Autowired
	private SheetLogMapper sheetLogMapper;
	
	
	public void save(SheetLog entity){
		sheetLogMapper.save(entity);
	}
	

}
