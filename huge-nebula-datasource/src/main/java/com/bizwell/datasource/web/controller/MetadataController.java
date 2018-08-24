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

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.bean.SheetMetadata;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.ExcelSheetInfoService;
import com.bizwell.datasource.service.SheetMetadataService;

/**
 * 文件上传的Controller Created by liujian on 2018/4/27.
 */
@Controller
public class MetadataController {
	
	private static Logger logger = LoggerFactory.getLogger(MetadataController.class);

	@Autowired
	private SheetMetadataService sheetMetadataService;
	
	@Autowired
	private ExcelSheetInfoService excelSheetInfoService;
	

    /**
     * 根据sheetId获取Metadata数据
     * @param tableName
     * @param sheetId
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/datasource/getMetadataBySheetId")
    @ResponseBody
    public ResponseJson getMetadataBySheetId(@RequestParam(required=true) Integer sheetId) {
    	logger.info("getMetadataBySheetId sheetId = " + sheetId );
   	
    	SheetMetadata metadata = new SheetMetadata();
    	metadata.setSheetId(sheetId);
    	metadata.setSort(" order by field_type DESC ");
    	List<SheetMetadata> metadataList = sheetMetadataService.select(metadata);
    	
    	String sheetName = "";
    	ExcelSheetInfo sheetInfo = new ExcelSheetInfo();
    	sheetInfo.setId(sheetId);
    	List<ExcelSheetInfo> sheetList = excelSheetInfoService.select(sheetInfo);
    	if(sheetList.size()>0){
    		sheetName = sheetList.get(0).getSheetName();
    	}
    	
    	Map result = new HashMap<>();
    	result.put("metadataList", metadataList);
    	result.put("sheetName", sheetName);
    	return new ResponseJson(200l,"success",result);
    }
    
    /**
     * 根据fieldColumn获取xls表中的数据
     * @param tableName
     * @param fieldColumn
     * @return
     */
    @RequestMapping(value = "/datasource/getXlsDataByFilter")
    @ResponseBody
    public ResponseJson getXlsDataByFilter(
    		@RequestParam(required=true) String tableName,
    		@RequestParam(required=true) String fieldColumn) {
    	
    	logger.info("getXlsDataByFilter tableName=" + tableName + "  fieldColumn="+fieldColumn);
    	
    	List<Map> xlsData = sheetMetadataService.getXlsDataByFilter(tableName, fieldColumn);
    	
    	Map result = new HashMap<>();
    	result.put("data", xlsData);
    	return new ResponseJson(200l,"success",result);
    }

    
    
    /**
     * 根据fieldColumn获取xls表中的数据
     * @param tableName
     * @param fieldColumn
     * @return
     */
    @RequestMapping(value = "/datasource/getXlsDataByDateFilter")
    @ResponseBody
    public ResponseJson getXlsDataByDateFilter(
    		@RequestParam(required=true) String tableName,
    		@RequestParam(required=true) String fieldColumn,
    		@RequestParam(required=true) String option) {
    	
    	logger.info("getXlsDataByDateFilter tableName=" + tableName + "  fieldColumn="+fieldColumn + "  option="+option);
    	
    	List<Map> data = sheetMetadataService.getXlsDataByDateFilter(tableName, fieldColumn,option);
    	
    	Map result = new HashMap<>();
    	result.put("data", data);
    	return new ResponseJson(200l,"success",result);
    }
    
    
    @RequestMapping(value = "/datasource/getXlsDataByNumberFilter")
    @ResponseBody
    public ResponseJson getXlsDataByNumberFilter(
    		@RequestParam(required=true) String tableName,
    		@RequestParam(required=true) String fieldColumn) {
    	
    	logger.info("getXlsDataByNumberFilter tableName=" + tableName + "  fieldColumn="+fieldColumn);
    	
    	List<Map> data = sheetMetadataService.getXlsDataByNumberFilter(tableName, fieldColumn);
    	
    	Map result = new HashMap<>();
    	result.put("data", data);
    	return new ResponseJson(200l,"success",result);
    }

    
    @RequestMapping(value = "/datasource/getXlsDataByConvergeFilter")
    @ResponseBody
    public ResponseJson getXlsDataByConvergeFilter(
    		@RequestParam(required=true) String tableName,
    		@RequestParam(required=true) String fieldColumn,
    		@RequestParam(required=true) String option) {
    	
    	logger.info("getXlsDataByConvergeFilter tableName=" + tableName + "  fieldColumn="+fieldColumn + "  option="+option);
    	
    	List<Map> data = sheetMetadataService.getXlsDataByConvergeFilter(tableName, fieldColumn,option);
    	
    	Map result = new HashMap<>();
    	result.put("data", data);
    	return new ResponseJson(200l,"success",result);
    }
    
}
