package com.bizwell.datasource.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.datasource.bean.FolderInfo;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.FolderInfoService;
import com.bizwell.datasource.web.BaseController;

/**
 * 创建sheet的Controller Created by liujian on 2018/4/27.
 */
@Controller
public class FolderController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(FolderController.class);

	@Autowired
	private FolderInfoService folderInfoService;
	

	/**
	 * 创建文件夹
	 * 
	 * @param folderName
	 * @return
	 */
	@RequestMapping(value = "/datasource/createFolder")
	@ResponseBody
	public ResponseJson createFolder(String folderName, Integer userId) {
		logger.info("createFolder  folderName=" + folderName + "  userId=" + userId);
		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setUserId(userId);
		folderInfo.setFolderName(folderName);
		folderInfo.setFolderType(2);
		// folderInfo.setParentId(parentId);
		folderInfo.setLevel(2);
		int folderId = folderInfoService.save(folderInfo);
		Map result = new HashMap();
		result.put("folderInfo", folderInfo);
		return new ResponseJson(200l, "success", result);
	}

	/**
	 * 获取文件夹
	 * 
	 * @param folderName
	 * @return
	 */
	@RequestMapping(value = "/datasource/getFolder")
	@ResponseBody
	public ResponseJson getFolder(String folderName, @RequestParam(defaultValue = "0") Integer userId) {
		logger.info("getFolder  folderName=" + folderName + "  userId=" + userId);
		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setFolderName(folderName);
		folderInfo.setUserId(userId);
		List<FolderInfo> list = folderInfoService.select(folderInfo);
		// logger.info("return"+JsonUtils.toJson(list));
		return new ResponseJson(200l, "success", list);
	}

	
	
	/**
	 * 删除folder
	 * 
	 * @param sheetId
	 * @return
	 */
	@RequestMapping(value = "/datasource/deleteFolder")
	@ResponseBody
	public ResponseJson deleteFolder(Integer floderId) {
		logger.info("deleteFolder  floderId =" + floderId);
		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setId(floderId);
		folderInfoService.delete(folderInfo);
		return new ResponseJson(200l, "success", null);
	}

}
