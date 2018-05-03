package com.bizwell.datasource.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.FolderInfo;
import com.bizwell.datasource.mapper.FolderInfoMapper;

@Service
public class ExcelFileInfoService {
	
	@Autowired
	private FolderInfoMapper folderInfoMapper;
	
	
	public void save(FolderInfo entity){
		folderInfoMapper.save(entity);
	}
}
