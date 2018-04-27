package com.bizwell.datasource.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bizwell.datasource.common.JsonUtils;
import com.bizwell.datasource.service.TicketService;
import com.bizwell.datasource.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.datasource.common.JsonView;
import com.bizwell.datasource.exception.PassportException;
import com.bizwell.datasource.exception.ResponseCode;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Controller
@RequestMapping(value="/logout")
public class LogoutController extends BaseController {
	
	@Autowired
	private TicketService ticketService;
	
	// 服务端登出 
	@RequestMapping(value = "/signout", method = RequestMethod.POST)
	@ResponseBody
	public String signout(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "ticketCode", required = true) String ticketCode) {
		
		JsonView jsonView = new JsonView();
		try {
			// 获取cookie的值
//			String ticket = WebUtils.getCookieValue(request, Constants.TICKET_COOKIE_NAME);
			
			if (StringUtils.isNotEmpty(ticketCode)) {
				// 清除cookie ticket
//				WebUtils.removeCookie(response, Constants.TICKET_COOKIE_NAME);
				// 清除缓存ticket
				ticketService.removeTicket(ticketCode);
			}
			jsonView = result(ResponseCode.SUCCESS.getCode(), ResponseCode.SUCCESS.getMessage(), null);
		} catch (PassportException e) {
			jsonView.setCode(e.getCode());
			jsonView.setMessage(e.getMessage());
		} catch (Exception e) {
			jsonView = result(ResponseCode.ERROR.getCode(), ResponseCode.ERROR.getMessage(), null);
			e.printStackTrace();
		}
		return JsonUtils.toJson(jsonView);
	}

}
