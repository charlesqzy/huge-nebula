package com.bizwell.datasource.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.FileMD5Util;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.service.ExcelFileInfoService;
import com.bizwell.datasource.service.JDBCService;
import com.bizwell.datasource.service.ReadExcelForHSSF;
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

	/**
	 * 文件上传具体实现方法（单文件上传）
	 *
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/datasource/uploadExcel", method = RequestMethod.POST)
	public @ResponseBody String uploadExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

		String contentType = file.getContentType();
		String fileName = file.getOriginalFilename();

		String filePath = request.getSession().getServletContext().getRealPath("excelfile/");
		logger.info("filePath=" + filePath);

		try {
			this.uploadFile(file.getBytes(), filePath, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//获取文件hash值
		String md5Hashcode = FileMD5Util.getFileMD5(new File(filePath+fileName));
		
	

		XlsContent xlsContent = null;
		try {
			xlsContent = readExcelForHSSF.readExcel(filePath, fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExcelFileInfo entity = new ExcelFileInfo();
		entity.setFileCode(md5Hashcode);
		entity.setFileName(fileName);
		entity.setFilePath(filePath);
		// entity.setFileSize(fileSize);
		entity.setFileRows((Integer) xlsContent.getFileRows());
		entity.setFileColumns((Integer) xlsContent.getFileColumns());		
		// entity.setUserId(userId);
		excelFileInfoService.save(entity);
		xlsContent.setExcelFileId(entity.getId());
		xlsContent.setFileCode(md5Hashcode);

		logger.info("content=" + JsonUtils.toJson(xlsContent));


		// 返回json
		return JsonUtils.toJson(xlsContent);
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
