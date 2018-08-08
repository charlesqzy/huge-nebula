package com.bizwell.passport.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.passport.bean.domain.Ticket;
import com.bizwell.passport.bean.vo.UserVo;
import com.bizwell.passport.common.JsonUtils;
import com.bizwell.passport.common.JsonView;
import com.bizwell.passport.common.WebUtils;
import com.bizwell.passport.exception.PassportException;
import com.bizwell.passport.exception.ResponseCode;
import com.bizwell.passport.service.TicketService;
import com.bizwell.passport.service.UserService;
import com.bizwell.passport.web.BaseController;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 登录系统所走controller
@Controller
@RequestMapping(value="/passport/login")
public class Login extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private TicketService ticketService;
	
	// 登录
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	@ResponseBody
	public String doLogin(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			@RequestParam(value = "userName", required = true) String userName,
			@RequestParam(value = "password", required = true) String password,
			@RequestParam(value = "system", required = false) String system) {
		
		JsonView jsonView = new JsonView();
		try {
			if (StringUtils.isEmpty(userName)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL01.getCode(), ResponseCode.PASSPORT_FAIL01.getMessage());
			}
			if (StringUtils.isEmpty(password)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL02.getCode(), ResponseCode.PASSPORT_FAIL02.getMessage());
			}
			
			UserVo userVo = userService.login(userName, password , system);
			String ip = WebUtils.getRemoteIp(request);
			// 生成ticket,并存储到redis
			String ticket = genterateTicket(ip, userVo);
			userVo.setTicketCode(ticket);
			
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), userVo);
		} catch (PassportException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		
		return JsonUtils.toJson(jsonView);
	}
	
	// 跨域方式cookie检查
	@RequestMapping(value = "/checkLogin", method = RequestMethod.POST)
	@ResponseBody
	public String check(HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(value = "ticketCode", required = true) String ticketCode) {
		
		JsonView jsonView = new JsonView();
		try {
			if (StringUtils.isEmpty(ticketCode)) {
				throw new PassportException(ResponseCode.PASSPORT_FAIL10.getCode(), ResponseCode.PASSPORT_FAIL10.getMessage());
			} else {
				// 校验ticket
				Ticket ticket = ticketService.validateTicket(ticketCode);
				if (ticket != null) {
					jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
				} else {
					throw new PassportException(ResponseCode.PASSPORT_FAIL06.getCode(), ResponseCode.PASSPORT_FAIL06.getMessage());
				}
			}
		} catch (PassportException e) {
			jsonView = result(e.getCode(), e.getMessage(), null);
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return JsonUtils.toJson(jsonView);
	}
	
	// 生成ticket,并存储到redis
	private String genterateTicket(String ip, UserVo userVo) {
		
		Ticket ticket = new Ticket();
		ticket.setUserIp(ip);
		ticket.setUserId(userVo.getId());
		ticket.setUserName(userVo.getUserName());
		ticket.setGenteateTime(System.currentTimeMillis());
		// 存储ticket
		String storeTicke = ticketService.storeTicke(ticket);
		return storeTicke;
	}
	
}
