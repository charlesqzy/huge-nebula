package com.bizwell.datasource.web.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.common.FileMD5Util;
import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.service.ExcelSheetInfoService;

/**
 * 文件上传的Controller Created by liujian on 2018/4/27.
 */
@Controller
public class TestController {

	@Autowired
	private ExcelSheetInfoService excelSheetInfoService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String upload() {
		System.out.println(true && true && false);

		return "ss";
	}

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
