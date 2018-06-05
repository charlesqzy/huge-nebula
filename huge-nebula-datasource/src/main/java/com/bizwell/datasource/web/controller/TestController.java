package com.bizwell.datasource.web.controller;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 文件上传的Controller Created by liujian on 2018/4/27.
 */
@Controller
public class TestController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String upload() {

		return "hello";
	}

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

}
