package com.bizwell.echarts.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bizwell.echarts.bean.domain.FolderInfo;
import com.bizwell.echarts.bean.dto.FolderParam;
import com.bizwell.echarts.bean.vo.FolderVo;
import com.bizwell.echarts.mapper.FolderInfoMapper;
import com.bizwell.echarts.service.FolderService;

/**
 * @author zhangjianjun
 * @date 2018年5月15日
 *
 */
// 文件或文件夹service
@Service
public class FolderServiceImpl implements FolderService {

	@Autowired
	private FolderInfoMapper folderInfoMapper;
	
	// 查询所有文件夹及其子文件
	@Override
	public List<FolderVo> selectFolder(Integer userId) {

		List<FolderVo> resultList = new ArrayList<FolderVo>();
		List<FolderInfo> list = folderInfoMapper.selectFolder(userId);
		for (FolderInfo folderInfo : list) {
			FolderVo folderVo = new FolderVo();
			BeanUtils.copyProperties(folderInfo, folderVo);
			resultList.add(folderVo);
		}
		return resultList;
	}

	// 删除文件
	@Transactional
	@Override
	public void deleteFolder(Integer id) {

		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setId(id);
		folderInfo.setIsDel(1);
		folderInfo.setUpdateTime(new Date());
		folderInfoMapper.updateByPrimaryKeySelective(folderInfo);
	}

	// 更新文件
	@Transactional
	@Override
	public void updateFolder(Integer id, Integer childId) {

		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setId(childId);
		folderInfo.setParentId(id);
		folderInfo.setUpdateTime(new Date());
		folderInfoMapper.updateByPrimaryKeySelective(folderInfo);
	}

	// 保存文件
	@Transactional
	@Override
	public FolderVo saveFolder(Integer userId, String folderName, Integer parentId, Integer level) {

		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setUserId(userId);
		folderInfo.setFolderName(folderName);
		folderInfo.setFolderType(1);
		folderInfo.setParentId(parentId);
		folderInfo.setLevel(level);
		folderInfoMapper.insertSelective(folderInfo);
		FolderVo folderVo = new FolderVo();
		BeanUtils.copyProperties(folderInfo, folderVo);
		
		return folderVo;
	}

	// 通过参数查询文件
	@Override
	public List<FolderInfo> selectByParam(FolderParam folderParam) {

		List<FolderInfo> list = folderInfoMapper.selectByParam(folderParam);
		return list;
	}

	// 更新分享状态
	@Transactional
	@Override
	public void updateStatus(Integer id, String status, String shareRemarks,Integer isHeaderShow) {

		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setId(id);
		folderInfo.setReserved1(status);
		folderInfo.setReserved2(shareRemarks);
		folderInfo.setIsHeaderShow(isHeaderShow);
		folderInfoMapper.updateByPrimaryKeySelective(folderInfo);
	}

	// 更新ShowMore,用于前端高亮显示
	@Override
	public void updateShowMore(Integer id, Boolean showMore) {

		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setId(id);
		folderInfo.setShowMore(showMore);
		folderInfoMapper.updateByPrimaryKeySelective(folderInfo);
	}
	
}
