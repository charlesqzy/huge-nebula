package com.bizwell.datasource.web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.service.ExcelSheetInfoService;

/**
 * 文件上传的Controller
 * Created by liujian on 2018/4/27.
 */
@Controller
public class TestController {

	
	@Autowired
	private ExcelSheetInfoService excelSheetInfoService;
    
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String upload() {
        System.out.println("111");

        List<Map> s1 = excelSheetInfoService.getSheetDataByTableName("xls_b2fb3a6797394401126660e420ace066_sheet_1",1,10);
        
        
        System.out.println(JsonUtils.toJson(s1));
        
        return "ss";
    }


  
}
