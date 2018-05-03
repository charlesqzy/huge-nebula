package com.bizwell.datasource.web.controller;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.bean.FolderInfo;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.service.ExcelSheetInfoService;
import com.bizwell.datasource.service.FolderInfoService;

/**
 * 创建sheet的Controller
 * Created by liujian on 2018/4/27.
 */
@Controller
public class ExcelSheetInfoController {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelSheetInfoController.class);
	
	@Autowired
	private ExcelSheetInfoService excelSheetInfoService;
	
	@Autowired
	private FolderInfoService folderInfoService;
	
	
	/**
	 * 创建sheet
	 * @param sheetName
	 * @return
	 */
    @RequestMapping(value = "/datasource/createSheet")
    public String createSheet(String sheetName) {
    	
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();    	
    	
//    	excelSheetInfo.setExcelFileId(excelFileId);
    	excelSheetInfo.setSheetName(sheetName);
//    	excelSheetInfo.setFolderId(folderId);
//    	excelSheetInfo.setCategoryFlag(categoryFlag);
//    	excelSheetInfo.setRemark(remark);
//    	excelSheetInfo.setTableName(tableName);
    	excelSheetInfo.setUpdateTime(new Date());
//    	excelSheetInfo.setUserId(userId);    	
    	excelSheetInfoService.save(excelSheetInfo);
    	
        return "  ";
    }
    
    
    
    @RequestMapping(value = "/datasource/getSheet")
    @ResponseBody
    public String getSheet(String sheetName) {
    	
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();    	
    	
//    	excelSheetInfo.setExcelFileId(excelFileId);
    	excelSheetInfo.setSheetName(sheetName);
//    	excelSheetInfo.setFolderId(folderId);
//    	excelSheetInfo.setCategoryFlag(categoryFlag);
//    	excelSheetInfo.setRemark(remark);
//    	excelSheetInfo.setTableName(tableName);
    	excelSheetInfo.setUpdateTime(new Date());
//    	excelSheetInfo.setUserId(userId);    	
    	List<ExcelSheetInfo> list = excelSheetInfoService.select(excelSheetInfo);
    	
    	logger.info("return"+JsonUtils.toJson(list));    	
        return JsonUtils.toJson(list);
    }
    
    
    
    
    /**
     * 创建文件夹
     * @param folderName
     * @return
     */
    @RequestMapping(value = "/datasource/createFolder")
    public String createFolder(String folderName) {

    	FolderInfo folderInfo = new FolderInfo();    	
    	folderInfo.setUserId(1);
    	folderInfo.setFolderName(folderName);
    	folderInfo.setFolderType(2);
    	//folderInfo.setParentId(parentId);
    	folderInfo.setLevel(1);   	
    	folderInfoService.save(folderInfo);
    	
        return "  ";
    }
    
    
    
    
    /**
     * 获取文件夹
     * @param folderName
     * @return
     */
    @RequestMapping(value = "/datasource/getFolder")
    @ResponseBody
    public String getFolder(String folderName) {
    	FolderInfo folderInfo = new FolderInfo();
    	folderInfo.setFolderName(folderName);
    	List<FolderInfo> list = folderInfoService.select(folderInfo);    	
    	
    	logger.info("return"+JsonUtils.toJson(list));
    	
        return JsonUtils.toJson(list);
    }

}
