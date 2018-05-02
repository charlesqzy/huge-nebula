package com.bizwell.web.controller;

import com.alibaba.fastjson.JSON;
import com.bizwell.entity.*;
import com.bizwell.mapper.ExcelFileInfoMapper;
import com.bizwell.service.*;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by liujian on 2017/10/25.
 */
@Controller
public class TestController {
    
    @Autowired
    ExcelFileInfoMapper excelFileInfoMapper;


    @RequestMapping("test")
    @ResponseBody
    String test() {
    
        System.out.println("111");
        ExcelFileInfo excelFileInfo = new ExcelFileInfo();
        excelFileInfo.setFileName("testName");
        excelFileInfoMapper.insert(excelFileInfo);
        
        String resultJson = "aa";
        return resultJson;
        //return new ResponseJson(200L, "成功", resultJson);
    }
   

}
