package com.bizwell.passport.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.passport.bean.domain.User;
import com.bizwell.passport.common.JsonUtils;
import com.bizwell.passport.common.JsonView;
import com.bizwell.passport.exception.PassportException;
import com.bizwell.passport.exception.ResponseCode;
import com.bizwell.passport.service.UserService;
import com.bizwell.passport.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 注册用户所走controller
@Controller
@RequestMapping(value="/passport/register")
public class RegisterController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	// 校验注册用户名是否重复
	@RequestMapping(value = "/check/userName", method = RequestMethod.POST)
	@ResponseBody
	public String checkLoginName(@RequestParam(value = "userName", required = true) String userName) {
		
		JsonView jsonView = new JsonView();
		try {
			if (StringUtils.isEmpty(userName)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL01.getCode(), ResponseCode.PASSPORT_FAIL01.getMessage());
			}
			
			User user = userService.getUser(userName);
			if (user != null) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL08.getCode(), ResponseCode.PASSPORT_FAIL08.getMessage());
			}
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (PassportException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return JsonUtils.toJson(jsonView);
	}
	
	// 注册新用户
	@RequestMapping(value = "/submit", method = RequestMethod.POST)
	@ResponseBody
	public String register(@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "companyName", required = false) String companyName,
			@RequestParam(value = "telephone", required = false) String telephone) {
		
		JsonView jsonView = new JsonView();
		try {
			if (StringUtils.isEmpty(userName)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL01.getCode(), ResponseCode.PASSPORT_FAIL01.getMessage());
			}
			if (StringUtils.isEmpty(password)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL02.getCode(), ResponseCode.PASSPORT_FAIL02.getMessage());
			}
			/*
			if (StringUtils.isEmpty(companyName)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL09.getCode(), ResponseCode.PASSPORT_FAIL09.getMessage());
			}
			*/
			
			// 判断用户是否存在
			User user = userService.getUser(userName);
			if (user != null) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL08.getCode(), ResponseCode.PASSPORT_FAIL08.getMessage());
			}
			
			userService.insertSelective(userName, password, companyName, telephone);
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (PassportException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return JsonUtils.toJson(jsonView);
	}
	
}
