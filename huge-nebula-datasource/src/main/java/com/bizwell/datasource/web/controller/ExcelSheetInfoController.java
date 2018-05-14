package com.bizwell.datasource.web.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.bean.FolderInfo;
import com.bizwell.datasource.bean.SheetInfo;
import com.bizwell.datasource.bean.SheetLog;
import com.bizwell.datasource.bean.SheetMetadata;
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.DateHelp;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.ExcelSheetInfoService;
import com.bizwell.datasource.service.FolderInfoService;
import com.bizwell.datasource.service.JDBCService;
import com.bizwell.datasource.service.ReadExcelForHSSF;
import com.bizwell.datasource.service.SheetLogService;
import com.bizwell.datasource.service.SheetMetadataService;
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
	private SheetMetadataService sheetMetadataService;
		
	@Autowired
	private ReadExcelForHSSF readExcelForHSSF;
	
	@Autowired
	private SheetLogService sheetLogService;

	@Autowired
	private JDBCService jdbcService;
	
	/**
	 * 创建sheet
	 * @param sheetName
	 * @return
	 */
    @RequestMapping(value = "/datasource/createSheet")
    @ResponseBody
    public ResponseJson createSheet(@RequestBody XlsContent xlsContent ,Integer userId, HttpServletRequest request) {
    	String filePath = request.getSession().getServletContext().getRealPath("excelfile/");
		logger.info("createSheet  userId ="+ userId+ "filePath=" + filePath);

    	ExcelSheetInfo excelSheetInfo = null;
    	SheetLog sheetLog= null;
    	
    	//String dateStr = new SimpleDateFormat("yyMMddHHmmss").format(new Date());    	
    	int i = 1;    	
    	SheetInfo[] sheets = xlsContent.getSheets();
    	
    	for(SheetInfo sheet : sheets){    		
        	String tableName = "xls_"+xlsContent.getFileCode()+"_sheet_"+ i++ ;
    		//动态创建mysql，插入数据    	        	
    		
    		excelSheetInfo = new ExcelSheetInfo();
    		excelSheetInfo.setTableName(tableName);    		
    		excelSheetInfoService.delete(excelSheetInfo);//覆盖原sheet，先删除
    		
    		
        	excelSheetInfo.setExcelFileId(xlsContent.getExcelFileId());    		
        	excelSheetInfo.setSheetName(sheet.getSheetName());
        	excelSheetInfo.setFolderId(sheet.getFolderId());
//        	excelSheetInfo.setCategoryFlag(categoryFlag);
//        	excelSheetInfo.setRemark(remark);
        	excelSheetInfo.setTableClumns(sheet.getTypeList().size());
        	excelSheetInfo.setTableRows(sheet.getTypeList().size());
        	excelSheetInfo.setUpdateTime(DateHelp.getStrTime(new Date()));
        	excelSheetInfo.setUserId(userId);
        	excelSheetInfoService.save(excelSheetInfo);

        	String dropSql = readExcelForHSSF.generateDropTableSQL(tableName);
        	String createSql=readExcelForHSSF.generateCreateTableSQL(sheet.getTypeList(),
        			sheet.getContentList(),tableName);
        	String metadataSQL=readExcelForHSSF.generateMetadataSQL(sheet.getTypeList(),
        			sheet.getContentList(),excelSheetInfo.getId());    
        	String insertSQL=readExcelForHSSF.generateInsertTableSQL(
        			sheet.getContentList(),tableName);        	
        	
        	
        	jdbcService.executeSql(dropSql);
        	jdbcService.executeSql(createSql);
        	jdbcService.executeSql(metadataSQL);
        	jdbcService.executeSql(insertSQL);        	
        	
        	sheetLog = new SheetLog();
        	sheetLog.setSheetId(excelSheetInfo.getId());
        	sheetLog.setUpdateTime(new Date());
        	sheetLog.setUpdateLog("new sheet ");
        	sheetLogService.save(sheetLog);
    	}
    	
    	
    	Map result = new HashMap();
    	result.put("excelSheetInfo", excelSheetInfo);
    	return new ResponseJson(200l,sheets[0].getSheetName(),result);
    }
    
    
    
    /**
     * 获取sheet数据
     * @param sheetName
     * @return
     */
    @RequestMapping(value = "/datasource/getSheet")
    @ResponseBody
    public ResponseJson getSheet(String sheetName) {
    	logger.info("getSheet  sheetName ="+ sheetName);
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();    	
    	excelSheetInfo.setSheetName(sheetName);
    	excelSheetInfo.setUpdateTime(DateHelp.getStrTime(new Date()));
    	List<ExcelSheetInfo> list = excelSheetInfoService.select(excelSheetInfo);
    	
    	return new ResponseJson(200l,"success",list);
    }
    
    

    /**
     * 删除sheet
     * @param sheetId
     * @return
     */
    @RequestMapping(value = "/datasource/deleteSheet")
    @ResponseBody
    public ResponseJson deleteSheet(Integer sheetId) {
    	logger.info("deleteSheet  sheetId ="+ sheetId);
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();    	
    	excelSheetInfo.setId(sheetId);
    	excelSheetInfoService.delete(excelSheetInfo);
    	return new ResponseJson(200l,"success",null);
    }
    
    
    
    /**
     * 移动sheet
     * @param sheetId
     * @return
     */
    @RequestMapping(value = "/datasource/moveSheet")
    @ResponseBody
    public ResponseJson moveSheet(Integer sheetId,Integer targetFolderId) { 
    	logger.info("moveSheet  sheetId ="+ sheetId + "  targetFolderId="+targetFolderId);
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();    	
    	excelSheetInfo.setId(sheetId);
    	excelSheetInfo.setFolderId(targetFolderId);
    	excelSheetInfoService.update(excelSheetInfo);
    	return new ResponseJson(200l,"success",null);
    }
    
    
    
    
    
    /**
     * 根据文件夹id获取sheet
     * @param folderId
     * @return
     */
    @RequestMapping(value = "/datasource/getSheetByFolderId")
    @ResponseBody
    public ResponseJson getSheetByFolderId(Integer folderId) {
    	logger.info("getSheetByFolderId  folderId ="+ folderId );
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo(); 
    	excelSheetInfo.setFolderId(folderId);
    	List<ExcelSheetInfo> list = excelSheetInfoService.select(excelSheetInfo);
    	
    	return new ResponseJson(200l,"success",list);
    }
    
    
    /**
     * 根据tableName获取数据
     * @param tableName
     * @param sheetId
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/datasource/getSheetDataByTableName")
    @ResponseBody
    public ResponseJson getSheetDataByTableName(
    		@RequestParam String tableName,
    		@RequestParam Integer sheetId,
    		@RequestParam(defaultValue = "1") Integer pageNum,
    		@RequestParam(defaultValue = "20") Integer pageSize) {
    	
    	logger.info("getSheetDataByTableName.tableName=   "  +tableName + "  sheetId = " + sheetId +"  pageNum = "+pageNum);
    	List<Map> sheetList = excelSheetInfoService.getSheetDataByTableName(tableName,(pageNum-1)*pageSize,pageSize);    	
    	Integer totalRows = excelSheetInfoService.getCountByTableName(tableName);
    	
    	SheetMetadata entity = new SheetMetadata();
    	entity.setSheetId(sheetId);
    	List<SheetMetadata> metadataList = sheetMetadataService.select(entity);
    	XLSHaderType headerType = null;
    	List<XLSHaderType> haderList = new ArrayList();
    	for(SheetMetadata data : metadataList){
    		headerType = new XLSHaderType();
    		headerType.setProp(data.getFieldColumn());
    		headerType.setLabel(data.getFieldNameNew());
    		haderList.add(headerType);
    	}
    	
    	Map result = new HashMap<>();
    	result.put("sheet", sheetList);
    	result.put("header", haderList);
    	result.put("totalRows", totalRows);
    	result.put("pageNum", pageNum);
    	
    	logger.info("result =   "  + JsonUtils.toJson(result));
    	return new ResponseJson(200l,"success",result);
    }
    
    
    
    /**
     * 创建文件夹
     * @param folderName
     * @return
     */
    @RequestMapping(value = "/datasource/createFolder")
    @ResponseBody
    public ResponseJson createFolder(String folderName,Integer userId) {
    	logger.info("createFolder  folderName="+folderName +"  userId="+userId);
    	FolderInfo folderInfo = new FolderInfo();
    	folderInfo.setUserId(userId);
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
    public ResponseJson getFolder(String folderName,Integer userId) {
    	logger.info("getFolder  folderName="+folderName +"  userId="+userId);
    	FolderInfo folderInfo = new FolderInfo();
    	folderInfo.setFolderName(folderName);
    	folderInfo.setUserId(userId);
    	List<FolderInfo> list = folderInfoService.select(folderInfo);
    	//logger.info("return"+JsonUtils.toJson(list));
    	return new ResponseJson(200l,"success",list);
    }

}
