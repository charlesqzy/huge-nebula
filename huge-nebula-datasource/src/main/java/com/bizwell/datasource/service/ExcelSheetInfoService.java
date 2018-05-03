package com.bizwell.datasource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.mapper.ExcelSheetInfoMapper;

@Service
public class ExcelSheetInfoService {
	
	@Autowired
	private ExcelSheetInfoMapper excelSheetMapper;
	
	
	public void save(ExcelSheetInfo entity){
		excelSheetMapper.save(entity);
	}
	
	
	public List<ExcelSheetInfo> select(ExcelSheetInfo entity){
		return excelSheetMapper.select(entity);
	}
}
