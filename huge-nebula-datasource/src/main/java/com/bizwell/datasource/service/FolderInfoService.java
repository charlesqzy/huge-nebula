package com.bizwell.datasource.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.FolderInfo;
import com.bizwell.datasource.mapper.FolderInfoMapper;

@Service
public class FolderInfoService {
	
	@Autowired
	private FolderInfoMapper folderInfoMapper;
	
	
	public void save(FolderInfo entity){
		folderInfoMapper.save(entity);
	}
	
	
	public List<FolderInfo> select(FolderInfo entity){
		return folderInfoMapper.select(entity);
	}
}
