package com.bizwell.echarts.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.echarts.service.JDBCService;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Controller
@RequestMapping(value="/user")
public class Login {
	
	@Autowired
	private JDBCService JDBCService;
	
	@RequestMapping(value = "/getUser", method = RequestMethod.GET)
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {
		
//		JDBCService.getUser();
		return "aaa";
	}
	
	
}
