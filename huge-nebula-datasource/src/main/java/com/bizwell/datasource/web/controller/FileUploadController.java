package com.bizwell.datasource.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bizwell.datasource.common.ReadExcelForHSSF;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 文件上传的Controller
 * Created by liujian on 2018/4/27.
 */
@Controller
public class FileUploadController {
	
	private static Logger logger = LoggerFactory.getLogger(FileUploadController.class);
	
	
    @RequestMapping(value = "/datasource/upload", method = RequestMethod.GET)
    public String upload() {
        return "/fileupload";
    }

    
    /**
     * 文件上传具体实现方法（单文件上传）
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/datasource/uploadExcel", method = RequestMethod.POST)
    public @ResponseBody String uploadExcel(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) {
    	
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        String filePath = request.getSession().getServletContext().getRealPath("excelfile/");
        logger.info("filePath="+filePath);
        
        try {
            this.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String content = "nothoing";
        try {
        	content = ReadExcelForHSSF.readExcel(filePath,fileName);        	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        logger.info("content="+content);
        
        //返回json
        return content;
    }
    
    
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath+fileName);
        out.write(file);
        out.flush();
        out.close();
    }
  
}
