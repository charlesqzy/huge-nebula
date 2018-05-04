package com.bizwell.datasource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.SheetMetadata;
import com.bizwell.datasource.mapper.SheetMetadataMapper;

@Service
public class SheetMetadataService {
	
	@Autowired
	private SheetMetadataMapper sheetMetadataMapper;
	
	
	public void save(SheetMetadata entity){
		sheetMetadataMapper.save(entity);
	}
	
	
	public List<SheetMetadata> select(SheetMetadata entity){
		return sheetMetadataMapper.select(entity);
	}
}
