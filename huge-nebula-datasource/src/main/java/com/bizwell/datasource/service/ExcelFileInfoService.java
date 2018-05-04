package com.bizwell.datasource.service;

import java.util.List;

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
	
	
	public List<ExcelFileInfo> select(ExcelFileInfo entity){
		return excelFileInfoMapper.select(entity);
	}
}
