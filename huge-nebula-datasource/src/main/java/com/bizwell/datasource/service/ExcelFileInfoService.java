package com.bizwell.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.ExcelFileInfo;
import com.bizwell.datasource.mapper.ExcelFileInfoMapper;

@Service
public class ExcelFileInfoService {
	
	@Autowired
	private ExcelFileInfoMapper excelFileInfoMapper;
	
	
	public void save(ExcelFileInfo entity){
		excelFileInfoMapper.save(entity);
	}
}
