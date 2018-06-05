package com.bizwell.datasource.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.bizwell.datasource.service.MysqlHelper;
import com.bizwell.datasource.service.ReadCSVUtil;
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
	@RequestMapping(value = "/datasource/uploadExcel")
	public @ResponseBody ResponseJson uploadExcel(
			@RequestParam(value = "file", required = true) MultipartFile file,
			@RequestParam(defaultValue = "0") Integer userId, 
			HttpServletRequest request) {
		
		String fileName = file.getOriginalFilename();
		String filePath = request.getSession().getServletContext().getRealPath("excelfile/");

		logger.info("uploadExcel  userId=" + userId + "   filePath=" + filePath  +  "   fileName="+fileName);
		
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
		if (excelFileInfoService.select(entity).size() > 0) {
			code = 201L;
			message = "文件已存在,将会覆盖原文件";
		}

		try {
			if (fileName.indexOf(".xlsx") > -1 || fileName.indexOf(".xls") > -1) {// 判断文件类型
				xlsContent = readExcelForHSSF.readExcel(filePath, fileName, true);
			} else if (fileName.indexOf(".csv") > -1) {
				xlsContent = ReadCSVUtil.readCSV(filePath, fileName, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		entity.setFileName(fileName);
		entity.setFilePath(filePath);
		// entity.setFileSize(fileSize);
		entity.setFileRows(xlsContent.getFileRows());
		entity.setFileColumns(xlsContent.getFileColumns());
		entity.setUserId(userId);
		excelFileInfoService.save(entity);
		xlsContent.setExcelFileId(entity.getId());
		xlsContent.setFileCode(md5Hashcode);

		logger.info("content=" + JsonUtils.toJson(xlsContent));
		// 返回json
		// return JsonUtils.toJson(xlsContent);
		return new ResponseJson(code, message, xlsContent);
	}

	/**
	 * 追加数据 apppendUploadExcel
	 * 
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/datasource/apppendUploadExcel")
	public @ResponseBody ResponseJson apppendUploadExcel(
			@RequestParam(value = "file", required = true) MultipartFile file, 
			@RequestParam Integer sheetId,
			@RequestParam(defaultValue = "0") Integer userId, 
			@RequestParam(defaultValue = "false") Boolean replase,
			HttpServletRequest request) {

		logger.info("apppendUploadExcel   sheetId==" + sheetId + "  userId=" + userId + " replase=" + replase);
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
			xlsContent = readExcelForHSSF.readExcel(filePath, fileName, false);
		} catch (IOException e) {
			e.printStackTrace();
		}

		logger.info("sheetMetaList.size() == " + sheetMetaList.size()
				+ "        xlsContent.getSheets()[0].getTypeList().size()="
				+ xlsContent.getSheets()[0].getTypeList().size());
		if (sheetMetaList.size() != xlsContent.getSheets()[0].getTypeList().size()) {
			return new ResponseJson(202L, "文件不匹配，无法追加!", null);
		}

		boolean flag = true;

		ExcelSheetInfo excelSheetInfo = new ExcelSheetInfo();
		excelSheetInfo.setId(sheetId);
		List<ExcelSheetInfo> sheetInfo = excelSheetInfoService.select(excelSheetInfo);
		if (!sheetInfo.isEmpty()) {
			String tableName = sheetInfo.get(0).getTableName();

			String msg = "append sheet";
			if (replase) {
				String truncateSql = MysqlHelper.generateTruncateTableSQL(tableName);
				jdbcService.executeSql(truncateSql);
				msg="replase sheet";
			}

			String insertSQL = MysqlHelper.generateInsertTableSQL(xlsContent.getSheets()[0].getContentList(),
					tableName);
			flag = jdbcService.executeSql(insertSQL);

			SheetLog sheetLog = new SheetLog();
			sheetLog.setSheetId(excelSheetInfo.getId());
			sheetLog.setUpdateTime(new Date());
			sheetLog.setUpdateLog(msg);
			sheetLogService.save(sheetLog);
		}

		logger.info("flag = === =" + flag);
		return new ResponseJson(200L, "success", null);
		// if(flag){
		// }else{
		// return new ResponseJson(203L,"fail",null);
		// }
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
