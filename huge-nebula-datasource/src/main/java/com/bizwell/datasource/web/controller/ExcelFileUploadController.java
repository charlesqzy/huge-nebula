package com.bizwell.datasource.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bizwell.datasource.bean.ExcelFileInfo;
import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.bean.SheetLog;
import com.bizwell.datasource.bean.SheetMetadata;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.FileMD5Util;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.ExcelFileInfoService;
import com.bizwell.datasource.service.ExcelSheetInfoService;
import com.bizwell.datasource.service.JDBCService;
import com.bizwell.datasource.service.ReadExcelForHSSF;
import com.bizwell.datasource.service.SheetLogService;
import com.bizwell.datasource.service.SheetMetadataService;
import com.bizwell.datasource.web.BaseController;

/**
 * 文件上传的Controller Created by liujian on 2018/4/27.
 */
@Controller
public class ExcelFileUploadController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(ExcelFileUploadController.class);

	@Autowired
	private ExcelFileInfoService excelFileInfoService;
	

	@Autowired
	private ReadExcelForHSSF readExcelForHSSF;
	
	@Autowired
	private SheetMetadataService sheetMetadataService;
	
	@Autowired
	private ExcelSheetInfoService excelSheetInfoService;
	
	@Autowired
	private JDBCService jdbcService;
	
	@Autowired
	private SheetLogService sheetLogService;
	
	/**
	 * 文件上传具体实现方法（单文件上传）
	 *
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/datasource/uploadExcel", method = RequestMethod.POST)	
	public @ResponseBody ResponseJson uploadExcel(@RequestParam("file") MultipartFile file,Integer userId, HttpServletRequest request) {

		//String contentType = file.getContentType();
		String fileName = file.getOriginalFilename();

		String filePath = request.getSession().getServletContext().getRealPath("excelfile/");
		logger.info("filePath=" + filePath);

		try {
			this.uploadFile(file.getBytes(), filePath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}

		XlsContent xlsContent = null;

		
		Long code = 200L;
		String message = "success";
		
		// 获取文件hash值
		String md5Hashcode = FileMD5Util.getFileMD5(new File(filePath + fileName));
		ExcelFileInfo entity = new ExcelFileInfo();
		entity.setFileCode(md5Hashcode);
		if (null != excelFileInfoService.select(entity)) {
			//return new ResponseJson(201l,"文件已存在",null);
			code = 201L;
			message = "文件已存在,将会覆盖原文件";
		}
		
		try {
			xlsContent = readExcelForHSSF.readExcel(filePath, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		entity.setFileCode(md5Hashcode);
		entity.setFileName(fileName);
		entity.setFilePath(filePath);
		// entity.setFileSize(fileSize);
		entity.setFileRows((Integer) xlsContent.getFileRows());
		entity.setFileColumns((Integer) xlsContent.getFileColumns());
		entity.setUserId(userId);
		excelFileInfoService.save(entity);
		xlsContent.setExcelFileId(entity.getId());
		xlsContent.setFileCode(md5Hashcode);
		logger.info("content=" + JsonUtils.toJson(xlsContent));
		// 返回json
		//return JsonUtils.toJson(xlsContent);
		return new ResponseJson(code,message,xlsContent);


	}
	
	
	
	
	
	
	
	/**
	 * 追加数据
	 *
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/datasource/apppendUploadExcel", method = RequestMethod.POST)	
	public @ResponseBody ResponseJson apppendUploadExcel(@RequestParam("file") MultipartFile file,Integer sheetId,Integer userId, HttpServletRequest request) {

		//String contentType = file.getContentType();
		String fileName = file.getOriginalFilename();

		String filePath = request.getSession().getServletContext().getRealPath("excelfile/");
		logger.info("filePath=" + filePath);

		try {
			this.uploadFile(file.getBytes(), filePath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		SheetMetadata sheetMetadata = new SheetMetadata();
		sheetMetadata.setSheetId(sheetId);
		List<SheetMetadata> sheetMetaList = sheetMetadataService.select(sheetMetadata);
		
		XlsContent xlsContent = null;
		try {
			xlsContent = readExcelForHSSF.readExcel(filePath, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(sheetMetaList.size() != xlsContent.getSheets()[0].getTypeList().size()){
			return new ResponseJson(202L,"文件不匹配，无法追加",null);
		}
		
		
		
		ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();
		excelSheetInfo.setId(sheetId);
		List<ExcelSheetInfo> sheetInfo = excelSheetInfoService.select(excelSheetInfo);
		if(!sheetInfo.isEmpty()){
			String tableName = sheetInfo.get(0).getTableName();
			String insertSQL = readExcelForHSSF.generateInsertTableSQL(xlsContent.getSheets()[0].getContentList(), tableName);
			jdbcService.executeSql(insertSQL);
			
			
			SheetLog sheetLog = new SheetLog();
        	sheetLog.setSheetId(excelSheetInfo.getId());
        	sheetLog.setUpdateTime(new Date());
        	sheetLog.setUpdateLog("append sheet ");
        	sheetLogService.save(sheetLog);
		}
		

		

		// 返回json
		//return JsonUtils.toJson(xlsContent);
		return new ResponseJson(200L,"success",null);
	}
	
	

	public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
		File targetFile = new File(filePath);
		if (!targetFile.exists()) {
			targetFile.mkdirs();
		}
		FileOutputStream out = new FileOutputStream(filePath + fileName);
		out.write(file);
		out.flush();
		out.close();
	}

}
