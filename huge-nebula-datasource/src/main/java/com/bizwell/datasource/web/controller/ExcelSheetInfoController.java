package com.bizwell.datasource.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import com.bizwell.datasource.common.HttpClientUtil;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.ExcelSheetInfoService;
import com.bizwell.datasource.service.FolderInfoService;
import com.bizwell.datasource.service.JDBCService;
import com.bizwell.datasource.service.MysqlHelper;
import com.bizwell.datasource.service.ReadCSVUtil;
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
	private SheetMetadataService sheetMetadataService;
		
	@Autowired
	private ReadExcelForHSSF readExcelForHSSF;
	
	@Autowired
	private SheetLogService sheetLogService;

	@Autowired
	private JDBCService jdbcService;
	
	
	@Value("${echarts.metadata.refresh}")
	String httpUrl = "";
	
	/**
	 * 创建sheet
	 * @param sheetName
	 * @return
	 */
    @RequestMapping(value = "/datasource/createSheet")
    @ResponseBody
    public ResponseJson createSheet(@RequestBody XlsContent xlsContent ,
    		HttpServletRequest request) {
    	Integer userId= xlsContent.getUserId();
    	String fileName = xlsContent.getFileName();
    	String fileCode = xlsContent.getFileCode();
    	String filePath = request.getSession().getServletContext().getRealPath("excelfile/")+userId+"/";
    	
		
    	
		logger.info("createSheet userid="+userId+"  filePath=" + filePath + "  fileName="+fileName);
		
		long returnCode = 200L;
		String returnMsg = "success";
		
		XlsContent newXlsContent = null;
		try {
			if (fileName.indexOf(".xlsx") > -1 || fileName.indexOf(".xls") > -1) {// 判断文件类型
				newXlsContent =  readExcelForHSSF.readExcel(filePath, fileName, false);
			} else if (fileName.indexOf(".csv") > -1) {
				newXlsContent = ReadCSVUtil.readCSV(filePath, fileName, false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

    	ExcelSheetInfo excelSheetInfo = null;
    	SheetLog sheetLog= null;
    	
    	
    	int s = 0;
    	SheetInfo[] sheets = newXlsContent.getSheets();
    	
    	for(SheetInfo sheet : sheets){    	
    		if(sheet.getTypeList().size()==0){
    			continue;//如果没有数据则跳过
    		}
    		
        	String tableName = "xls_"+fileCode+"_u"+userId +"_s"+ s+1;
    		//动态创建mysql，插入数据    	        	
    		
    		excelSheetInfo = new ExcelSheetInfo();
    		excelSheetInfo.setTableName(tableName);    		
    		excelSheetInfoService.delete(excelSheetInfo);//覆盖原sheet，先删除    		
    		
        	excelSheetInfo.setExcelFileId(xlsContent.getExcelFileId());    		
        	excelSheetInfo.setSheetName(xlsContent.getSheets()[s].getSheetName());
        	excelSheetInfo.setFolderId(xlsContent.getSheets()[s].getFolderId());
//        	excelSheetInfo.setCategoryFlag(categoryFlag);
//        	excelSheetInfo.setRemark(remark);
        	excelSheetInfo.setTableClumns(sheet.getTypeList().size());
        	excelSheetInfo.setTableRows(sheet.getContentList().size());
        	excelSheetInfo.setUpdateTime(DateHelp.getStrTime(new Date()));
        	excelSheetInfo.setUserId(userId);
        	excelSheetInfoService.save(excelSheetInfo);

        	String dropSql = MysqlHelper.generateDropTableSQL(tableName);
        	String createSql=MysqlHelper.generateCreateTableSQL(xlsContent.getSheets()[s].getTypeList(), tableName);
        	Integer rowIndex = xlsContent.getSheets()[s].getRowIndex();
        	Integer startRow = rowIndex>0?rowIndex-1:rowIndex ;
        	System.out.println("startRow===="+startRow);
        	String insertSQL=MysqlHelper.generateInsertTableSQL(
        			sheet.getContentList().subList(startRow, sheet.getContentList().size()),tableName);
        	
        	String deleteMetadataSQL=MysqlHelper.generateDeleteMetadataSQL(tableName);
        	//取页面传来的header类型
        	String insertMetadataSQL=MysqlHelper.generateInsertMetadataSQL(xlsContent.getSheets()[s].getTypeList(),
        			sheet.getContentList(),excelSheetInfo.getId(),tableName,userId);
        	
        	boolean flag1 =jdbcService.executeSql(dropSql);
        	boolean flag2 =jdbcService.executeSql(createSql);
        	boolean flag3 =jdbcService.executeSql(insertSQL);
        	
        	boolean flag4 =jdbcService.executeSql(deleteMetadataSQL);
        	boolean flag5 =jdbcService.executeSql(insertMetadataSQL);
        	
        	if(!(flag1&&flag2&&flag3&&flag4&&flag5)){
        		returnCode = 202;
        		returnMsg = "error";
        		break;
        	}
        	
        	sheetLog = new SheetLog();
        	sheetLog.setSheetId(excelSheetInfo.getId());
        	sheetLog.setUpdateTime(new Date());
        	sheetLog.setUpdateLog("new sheet ");
        	sheetLogService.save(sheetLog);
        	
        	
        	/* requset echarts project */
        	/*
        	logger.info("createSheet httpUrl="+httpUrl);
    		Map data = new HashMap<>();
    		data.put("userId", userId+"");
    		HttpClientUtil.sendHttpPost(httpUrl, data);
        	*/
        	s++;
    	}
    	
    	
    	Map result = new HashMap();
    	result.put("excelSheetInfo", excelSheetInfo);
    	return new ResponseJson(returnCode,returnMsg,result);
    }
    
    
    
    /**
     * 获取sheet数据
     * @param sheetName
     * @return
     */
    @RequestMapping(value = "/datasource/getSheet")
    @ResponseBody
    public ResponseJson getSheet(
    		@RequestParam(required=true)Integer userId,
    		@RequestParam(required=false)String sheetName) {
    	logger.info("getSheet  userId="+userId+" sheetName ="+ sheetName);
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();
    	excelSheetInfo.setUserId(userId);
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
    public ResponseJson deleteSheet(@RequestParam(required=true)Integer sheetId) {
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
    public ResponseJson moveSheet(
    		@RequestParam(required=true)Integer sheetId,
    		@RequestParam(required=true)Integer targetFolderId) { 
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
    public ResponseJson getSheetByFolderId(
    		@RequestParam(required=false)Integer folderId,
    		@RequestParam(required=false)Integer userId) {
    	logger.info("getSheetByFolderId  folderId ="+ folderId );
    	ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo(); 
    	excelSheetInfo.setFolderId(folderId);
    	excelSheetInfo.setUserId(userId);
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
    public String getSheetDataByTableName(
    		@RequestParam(required=true) String tableName,
    		@RequestParam(required=true) Integer sheetId,
    		@RequestParam(defaultValue = "1") Integer pageNum,
    		@RequestParam(defaultValue = "20") Integer pageSize) {
    	
    	logger.info("getSheetDataByTableName.tableName= "  +tableName + "  sheetId = " + sheetId +"  pageNum = "+pageNum);
    	List<Map<String,String>> sheetList = excelSheetInfoService.getSheetDataByTableName(tableName,(pageNum-1)*pageSize,pageSize);    	
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
    		headerType.setType(data.getFieldType()+"");
    		haderList.add(headerType);
    	}
    	
    	Map result = new HashMap<>();
    	result.put("sheet", sheetList);
    	result.put("header", haderList);
    	result.put("totalRows", totalRows);
    	result.put("pageNum", pageNum);
    	//return new ResponseJson(200l,"success",result);
    	
    	
    	Map m = new HashMap();
    	m.put("code", 200);
    	m.put("message", "success");
    	m.put("data", result);
    	logger.info("result =   "  + JsonUtils.toJson(m));
    	
    	return JsonUtils.toJson(m);
    }

}
