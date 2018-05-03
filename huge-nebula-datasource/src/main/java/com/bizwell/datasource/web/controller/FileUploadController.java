package com.bizwell.datasource.web.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.common.ReadExcelForHSSF;
import com.bizwell.datasource.service.ExcelFileInfoService;

/**
 * 文件上传的Controller Created by liujian on 2018/4/27.
 */
@Controller
public class FileUploadController {

	private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@Autowired
	private ExcelFileInfoService excelFileInfoService;

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

		Map xlsContent = null;
		try {
			xlsContent = ReadExcelForHSSF.readExcel(filePath, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ExcelFileInfo entity = new ExcelFileInfo();
		// entity.setFileCode(fileCode);
		entity.setFileName(fileName);
		entity.setFilePath(filePath);
		// entity.setFileSize(fileSize);
		entity.setFileRows((Integer) xlsContent.get("fileRows"));
		entity.setFileColumns((Integer) xlsContent.get("fileColumns"));
		// entity.setUserId(userId);
		excelFileInfoService.save(entity);

		
		

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
