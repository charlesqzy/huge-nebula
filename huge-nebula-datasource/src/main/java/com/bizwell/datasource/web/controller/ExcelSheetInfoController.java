package com.bizwell.datasource.web.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.bean.FolderInfo;
import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.ExcelSheetInfoService;
import com.bizwell.datasource.service.FolderInfoService;
import com.bizwell.datasource.service.JDBCService;
import com.bizwell.datasource.service.ReadExcelForHSSF;
import com.bizwell.datasource.web.BaseController;

/**
 * 创建sheet的Controller
 * Created by liujian on 2018/4/27.
 */
@Controller
public class ExcelSheetInfoController extends BaseController {
	
	private static Logger logger = LoggerFactory.getLogger(ExcelSheetInfoController.class);
	
	@Autowired
	private ExcelSheetInfoService excelSheetInfoService;
	
	@Autowired
	private FolderInfoService folderInfoService;
		
	@Autowired
	private ReadExcelForHSSF readExcelForHSSF;

	@Autowired
	private JDBCService jdbcService;
	
	/**
	 * 创建sheet
	 * @param sheetName
	 * @return
	 */
    @RequestMapping(value = "/datasource/createSheet")
    @ResponseBody
    public ResponseJson createSheet(@RequestBody XlsContent xlsContent, HttpServletRequest request) {
    	String filePath = request.getSession().getServletContext().getRealPath("excelfile/");
		logger.info("filePath=" + filePath);

    	ExcelSheetInfo excelSheetInfo = null;    	
    	
    	SheetInfo[] sheets = xlsContent.getSheets();
    	
    	
    	String dateStr = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
    	
    	int i = 1;
    	
    	for(SheetInfo sheet : sheets){    		
        	String tableName = "xls_"+ dateStr+"_sheet"+ i++ +"_"+xlsContent.getFileCode();
    		//动态创建mysql，插入数据    	        	
    		
    		excelSheetInfo = new ExcelSheetInfo();
        	excelSheetInfo.setExcelFileId(xlsContent.getExcelFileId());    		
        	excelSheetInfo.setSheetName(sheet.getSheetName());
        	excelSheetInfo.setFolderId(sheet.getFolderId());
//        	excelSheetInfo.setCategoryFlag(categoryFlag);
//        	excelSheetInfo.setRemark(remark);
        	excelSheetInfo.setTableName(tableName);
        	excelSheetInfo.setUpdateTime(new Date());
//        	excelSheetInfo.setUserId(userId);
        	excelSheetInfoService.save(excelSheetInfo);

        	String createSql=readExcelForHSSF.generateCreateTableSQL(sheet.getTypeList(),
        			sheet.getContentList(),tableName);
        	String metadataSQL=readExcelForHSSF.generateMetadataSQL(sheet.getTypeList(),
        			sheet.getContentList(),excelSheetInfo.getId());    
        	String insertSQL=readExcelForHSSF.generateInsertTableSQL(
        			sheet.getContentList(),tableName);
        	
        	jdbcService.executeSql(createSql);
        	jdbcService.executeSql(metadataSQL);
        	jdbcService.executeSql(insertSQL);
    	}
    	
    	Map result = new HashMap();
    	result.put("excelSheetInfo", excelSheetInfo);
    	return new ResponseJson(200l,sheets[0].getSheetName(),result);
    }
    
    
    
    @RequestMapping(value = "/datasource/getSheet")
    @ResponseBody
    public ResponseJson getSheet(String sheetName) {
    	
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
    	
    	return new ResponseJson(200l,"success",list);
    }
    
    
    
    
    /**
     * 创建文件夹
     * @param folderName
     * @return
     */
    @RequestMapping(value = "/datasource/createFolder")
    @ResponseBody
    public ResponseJson createFolder(String folderName) {

    	FolderInfo folderInfo = new FolderInfo();    	
    	folderInfo.setUserId(1);
    	folderInfo.setFolderName(folderName);
    	folderInfo.setFolderType(2);
    	//folderInfo.setParentId(parentId);
    	folderInfo.setLevel(1);
    	int folderId = folderInfoService.save(folderInfo);
    	Map result = new HashMap();    	
    	result.put("folderInfo", folderInfo);
    	return new ResponseJson(200l,"success",result);
    }
    
    
    
    
    /**
     * 获取文件夹
     * @param folderName
     * @return
     */
    @RequestMapping(value = "/datasource/getFolder")
    @ResponseBody
    public ResponseJson getFolder(String folderName) {
    	FolderInfo folderInfo = new FolderInfo();
    	folderInfo.setFolderName(folderName);
    	List<FolderInfo> list = folderInfoService.select(folderInfo);
    	//logger.info("return"+JsonUtils.toJson(list));
    	return new ResponseJson(200l,"success",list);
    }

}
