package com.bizwell.echarts.service;

import java.util.List;

import com.bizwell.echarts.bean.domain.FolderInfo;
import com.bizwell.echarts.bean.dto.FolderParam;
import com.bizwell.echarts.bean.vo.FolderVo;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
 *
 */
public interface FolderService {

	public List<FolderVo> selectFolder(Integer userId);
	
	public void deleteFolder(Integer id);
	
	public void updateFolder(Integer id, Integer childId);
	
	public FolderVo saveFolder(Integer userId, String folderName,Integer parentId, Integer level);
	
	public List<FolderInfo> selectByParam(FolderParam folderParam);
	
	public void updateStatus(Integer id, String status, String shareRemarks);
	
}
