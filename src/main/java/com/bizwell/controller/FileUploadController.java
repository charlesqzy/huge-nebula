package com.bizwell.controller;

import ch.qos.logback.core.util.FileUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;

/**
 * 文件上传的Controller
 * Created by liujian on 2018/4/27.
 */
@Controller
public class FileUploadController {
    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String upload() {
        return "/fileupload";
    }

    
    /**
     * 文件上传具体实现方法（单文件上传）
     *
     * @param file
     * @return
     */
    @RequestMapping(value="/testuploadimg", method = RequestMethod.POST)
    public @ResponseBody String uploadImg(@RequestParam("file") MultipartFile file,
                                          HttpServletRequest request) {
        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();

        String filePath = request.getSession().getServletContext().getRealPath("excelUpload/");
        try {
            this.uploadFile(file.getBytes(), filePath, fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回json
        return "uploadimg success";
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
