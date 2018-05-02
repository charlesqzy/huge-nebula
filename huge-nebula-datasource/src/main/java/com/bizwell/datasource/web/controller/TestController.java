package com.bizwell.datasource.web.controller;

import com.bizwell.datasource.bean.ExcelFileInfo;
import com.bizwell.datasource.mapper.ExcelFileInfoMapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 文件上传的Controller
 * Created by liujian on 2018/4/27.
 */
@Controller
public class TestController {
    @Autowired
    ExcelFileInfoMapper excelFileInfoMapper;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String upload() {
        System.out.println("111");
        ExcelFileInfo excelFileInfo = new ExcelFileInfo();
        excelFileInfo.setFileName("testName");
        excelFileInfoMapper.save(excelFileInfo);
        
        return "ss";
    }


  
}
