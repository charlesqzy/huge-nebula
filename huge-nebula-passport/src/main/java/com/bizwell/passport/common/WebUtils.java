package com.bizwell.passport.common;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public class WebUtils {

	private static String GET_METHOD = "GET";

	private static String POST_METHOD = "POST";

	private static String HTTP_HEADER_AJAX = "X-Requested-With";

	private static String HTTP_REQUEST_TYPE_AJAX = "XMLHttpRequest";

	public static boolean isAjaxRequest(HttpServletRequest request) {
		
		String requestType = request.getHeader(HTTP_HEADER_AJAX);
		if (requestType != null && requestType.equals(HTTP_REQUEST_TYPE_AJAX)) {
			return true;
		}
		return false;
	}

	public static boolean isGetMethod(HttpServletRequest request) {
		
		String method = request.getMethod();
		if (method != null && GET_METHOD.equals(method)) {
			return true;
		}
		return false;
	}

	public static boolean isPostMethod(HttpServletRequest request) {
		
		String method = request.getMethod();
		if (method != null && POST_METHOD.equals(method)) {
			return true;
		}
		return false;
	}

	public static String decodeUrl(String url, String charset) {
		
		String decodeURL = url;
		try {
			decodeURL = URLDecoder.decode(decodeURL, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return decodeURL;
	}

	// 获取用户ip
	public static String getRemoteIp(HttpServletRequest request) {
		
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
	// 获取用户指定cookie的值
	public static String getCookieValue(HttpServletRequest request, String cookieName) {
		
		String cookieValue = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(cookieName)) {
					cookieValue = cookie.getValue();
				}
			}
		}
		return cookieValue;
	}
	
	// 将code写入到cookie
	public static void writeToCookie(String name, String value, HttpServletResponse response) {
		
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
	// 删除cookie
	public static void removeCookie(HttpServletResponse response, String cookieName) {
		
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		response.addCookie(cookie);
	}
	
}
