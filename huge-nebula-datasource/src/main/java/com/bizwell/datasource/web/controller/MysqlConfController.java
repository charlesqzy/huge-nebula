package com.bizwell.datasource.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bizwell.datasource.bean.MysqlConnConf;
import com.bizwell.datasource.bean.MysqlTableConf;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.json.ResponseJson;
import com.bizwell.datasource.service.JDBCService;
import com.bizwell.datasource.service.MysqlConnConfService;
import com.bizwell.datasource.service.MysqlTableConfService;
import com.bizwell.datasource.web.BaseController;

/**
 * mysql数据源添加
 */
@Controller
public class MysqlConfController extends BaseController {

	private static Logger logger = LoggerFactory.getLogger(MysqlConfController.class);

	@Autowired
	private JDBCService jdbcService;

	@Autowired
	private MysqlConnConfService mysqlConnConfService;

	@Autowired
	private MysqlTableConfService mysqlTableConfService;

	/**
	 * 测试数据库连接是否可用
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/testMysqlConn")
	@ResponseBody
	public ResponseJson testMysqlConn(@RequestParam(required = true) String dbUrl,
			@RequestParam(required = true) String username, @RequestParam(required = true) String password) {
		logger.info("createMysqlConn dbUrl=" + dbUrl + "  username=" + username + "  password=" + password);

		boolean flag = jdbcService.testMysqlConn(dbUrl, username, password);

		Map result = new HashMap();
		result.put("flag", flag);
		return new ResponseJson(200l, "success", result);
	}

	/**
	 * 测试数据库连接是否可用
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/showDatabases")
	@ResponseBody
	public ResponseJson showDatabases(@RequestParam(required = true) String dbUrl,
			@RequestParam(required = true) String username, @RequestParam(required = true) String password) {
		logger.info("createMysqlConn dbUrl=" + dbUrl + "  username=" + username + "  password=" + password);

		List<String> list = jdbcService.showDatabases(dbUrl, username, password);

		Map result = new HashMap();
		result.put("list", list);
		return new ResponseJson(200l, "success", result);
	}

	/**
	 * 测试数据库连接是否可用
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/showTables")
	@ResponseBody
	public ResponseJson showTables(
			@RequestParam(required = true) Integer connId,
			@RequestParam(required = true) String dbUrl,
			@RequestParam(required = true) String username, 
			@RequestParam(required = true) String password,
			@RequestParam(required = true) String database,
			@RequestParam(required = true) Integer userId) {

		logger.info(" dbUrl=" + dbUrl + "  username=" + username + "  password=" + password
				+ " database=" + database);

		List<MysqlTableConf> list =jdbcService.showTables(connId, dbUrl, username, password ,database , userId);

		Map result = new HashMap();
		result.put("list", list);
		return new ResponseJson(200l, "success", result);
	}

	/**
	 * 创建数据库连接
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/createMysqlConn")
	@ResponseBody
	public ResponseJson createMysqlConn(
			@RequestParam(required = true) String name,
			@RequestParam(required = true) String dbUrl, 
			@RequestParam(required = true) String username,
			@RequestParam(required = true) String password,
			@RequestParam(required = true) Integer userId) {

		logger.info("createMysqlConn name=" + name + " dbUrl=" + dbUrl + "  userId=" + userId);

		MysqlConnConf entity = new MysqlConnConf();
		entity.setName(name);
		entity.setDbUrl(dbUrl);
		entity.setUsername(username);
		entity.setPassword(password);
		entity.setUserId(userId);
		mysqlConnConfService.save(entity);

		Map result = new HashMap();
		result.put("entity", entity);
		return new ResponseJson(200l, "success", result);
	}

	/**
	 * 获取数据库连接
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/getMysqlConn")
	@ResponseBody
	public ResponseJson getMysqlConn(@RequestParam(required = true) Integer userId) {
		logger.info("getMysqlConn userId=" + userId);
		MysqlConnConf entity = new MysqlConnConf();
		entity.setUserId(userId);
		List<MysqlConnConf> list = mysqlConnConfService.select(entity);
		return new ResponseJson(200l, "success", list);
	}

	/**
	 * 创建mysql工作表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/createMysqlTable")
	@ResponseBody
	public ResponseJson createMysqlTable(
			@RequestBody List<MysqlTableConf> list) {

		logger.info("createMysqlTable list.size() == "+list.size());
	
		mysqlTableConfService.save(list);

		Map result = new HashMap();
		result.put("list", list);
		return new ResponseJson(200l, "success", result);
	}

	/**
	 * 获取mysql工作表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/getMysqlTable")
	@ResponseBody
	public ResponseJson getMysqlTable(@RequestParam(required = true) Integer userId) {
		logger.info("getMysqlTable userId=" + userId);
		MysqlTableConf entity = new MysqlTableConf();
		entity.setUserId(userId);
		List<MysqlTableConf> list = mysqlTableConfService.select(entity);
		return new ResponseJson(200l, "success", list);
	}

}
