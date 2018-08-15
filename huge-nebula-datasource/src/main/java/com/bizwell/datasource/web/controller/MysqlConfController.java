package com.bizwell.datasource.web.controller;

import java.util.ArrayList;
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

import com.bizwell.datasource.bean.DatabaseInfo;
import com.bizwell.datasource.bean.ExcelSheetInfo;
import com.bizwell.datasource.bean.MysqlConnConf;
import com.bizwell.datasource.bean.MysqlTableConf;
import com.bizwell.datasource.bean.SheetMetadata;
import com.bizwell.datasource.bean.XlsContent;
import com.bizwell.datasource.common.JsonUtils;
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
			@RequestParam(required = true) String username, 
			@RequestParam(required = true) String password) {
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
			@RequestParam(required = true) String username, 
			@RequestParam(required = true) String password) {
		
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
	public ResponseJson getMysqlConn(@RequestParam(required = true) Integer userId,
			@RequestParam(required = true) Integer connId) {
		logger.info("getMysqlConn userId=" + userId);
		MysqlConnConf entity = new MysqlConnConf();
		entity.setUserId(userId);
		entity.setId(connId);
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
		
		List<Integer> connIds=new ArrayList<>();
		for(MysqlTableConf table : list){
		
			connIds.add(table.getConnId());
		}
		if(connIds.size()>0){
			mysqlTableConfService.deleteByConnId(connIds);
			
			jdbcService.setMysqlTableRowsClumns(list);//设置rows和clumns
			
			mysqlTableConfService.save(list);
		}
		logger.info("createMysqlTable list.size() == "+list.size());
	
		
		Map result = new HashMap();
		result.put("list", list);
		return new ResponseJson(200l, "success", result);
	}
	
	
	/**
	 * 获取mysql工作表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/getMysqlDatabase")
	@ResponseBody
	public ResponseJson getMysqlDatabase(
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) Integer connId) {
		logger.info("getMysqlTable userId=" + userId + "connId =" +connId);
		
		MysqlTableConf entity = new MysqlTableConf();
		entity.setUserId(userId);
		entity.setConnId(connId);
		List<DatabaseInfo> list = mysqlTableConfService.selectDatabase(entity);

		return new ResponseJson(200l, "success", list);
	}

	/**
	 * 获取mysql工作表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/datasource/getMysqlTable")
	@ResponseBody
	public ResponseJson getMysqlTable(
			@RequestParam(required = false) Integer userId,
			@RequestParam(required = false) Integer connId) {
		logger.info("getMysqlTable userId=" + userId + "connId =" +connId);
		
		MysqlTableConf entity = new MysqlTableConf();
		entity.setUserId(userId);
		entity.setConnId(connId);
		List<MysqlTableConf> list = mysqlTableConfService.select(entity);
		return new ResponseJson(200l, "success", list);
	}
	

	
	/**
	 * 获取mysql表的元数据
	 * @param connId
	 * @param tableName
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "/datasource/getMysqlDataByTableName")
	@ResponseBody
	public ResponseJson getMysqlDataByTableName(
			@RequestParam(required = true) Integer connId,
			@RequestParam(required = true) String databaseName,
			@RequestParam(required = true) String tableName,
			@RequestParam(required = true) Integer userId,
    		@RequestParam(defaultValue = "1") Integer pageNum,
    		@RequestParam(defaultValue = "20") Integer pageSize) {
		
		logger.info("getMysqlDataByTableName connId="+connId+" tableName="+tableName+" userId=" + userId );
		
		MysqlConnConf entity = new MysqlConnConf();
		entity.setUserId(userId);
		entity.setId(connId);		
		List<MysqlConnConf> list = mysqlConnConfService.select(entity);
		
		Map result = null;
		if(list.size()>0){
			MysqlConnConf conn = list.get(0);
			result = jdbcService.getMysqlTableData(conn,databaseName,tableName,pageNum,pageSize);
		}
    	
    	return new ResponseJson(200l, "success", result);
	}

	
    /**
     * 根据sheetId获取Metadata数据
     * @param tableName
     * @param sheetId
     * @param pageNum
     * @return
     */
    @RequestMapping(value = "/datasource/getMetadataByMysqlTableName")
    @ResponseBody
    public ResponseJson getMetadataByMysqlTableName(
			@RequestParam(required = true) Integer connId,
			@RequestParam(required = true) String databaseName,
    		@RequestParam(required = true) String tableName) {
    	
    	logger.info("getMetadataByMysqlTableName connId = " + connId + " databaseName="+databaseName + " tableName="+tableName );
    	
		MysqlConnConf conn = getConnConfById(connId);
    	
    	List<SheetMetadata> metadataList = jdbcService.getMetadataByMysqlTableName(conn,databaseName,tableName);;
    	
    	Map result = new HashMap<>();
    	result.put("metadataList", metadataList);
    	result.put("sheetName", tableName);
    	return new ResponseJson(200l,"success",result);
    }
    

    /**
     * 通过connId查连接配置信息
     * @param connId
     * @return
     */
	private MysqlConnConf getConnConfById(Integer connId) {
		MysqlConnConf entity = new MysqlConnConf();
		entity.setId(connId);		
		List<MysqlConnConf> list = mysqlConnConfService.select(entity);
		if(list.size()>0){
			return list.get(0);
		}else {
			return null;
		}
	}
	
	
	
    /**
     * 根据fieldColumn获取mysql表中的数据
     * @param tableName
     * @param fieldColumn
     * @return
     */
    @RequestMapping(value = "/datasource/getMysqlTableDataByFilter")
    @ResponseBody
    public ResponseJson getMysqlTableDataByFilter(
			@RequestParam(required = true) Integer connId,
			@RequestParam(required = true) String databaseName,
    		@RequestParam(required = true) String tableName,
    		@RequestParam(required = true) String fieldColumn) {
    	
     	logger.info("getMysqlTableDataByFilter connId = " + connId + "  databaseName="+databaseName + "  tableName="+tableName +"  fieldColumn="+fieldColumn);
       	
     	MysqlConnConf conn = getConnConfById(connId);
     	
		List<Map> mysqlData = null;
		mysqlData = jdbcService.getMysqlTableDataByFilter(conn,databaseName,tableName,fieldColumn);
    	
    	Map result = new HashMap<>();
    	result.put("mysqlData", mysqlData);
    	return new ResponseJson(200l,"success",result);
    }
    
    
    /**
     * 获取mysql表中的指定数字列的最大值和最小值
     * @param connId
     * @param databaseName
     * @param tableName
     * @param fieldColumn
     * @return
     */
    @RequestMapping(value = "/datasource/getMysqlTableDataByNumberFilter")
    @ResponseBody
    public ResponseJson getXlsDataByNumberFilter(
			@RequestParam(required = true) Integer connId,
			@RequestParam(required = true) String databaseName,
    		@RequestParam(required = true) String tableName,
    		@RequestParam(required = true) String fieldColumn) {
    	
     	logger.info("getMysqlTableDataByNumberFilter connId = " + connId + "  databaseName="+databaseName + " tableName="+tableName +" fieldColumn="+fieldColumn);
       	
     	MysqlConnConf conn = getConnConfById(connId);
     	
    	List<Map> data = jdbcService.getMysqlTableDataByNumberFilter(conn,databaseName, tableName, fieldColumn);
    	
    	Map result = new HashMap<>();
    	result.put("data", data);
    	return new ResponseJson(200l,"success",result);
    }

    
    
    @RequestMapping(value = "/datasource/getMysqlTableDataByConvergeFilter")
    @ResponseBody
    public ResponseJson getXlsDataByConvergeFilter(
    		@RequestParam(required = true) Integer connId,
    		@RequestParam(required = true) String databaseName,
    		@RequestParam(required = true) String tableName,
    		@RequestParam(required = true) String fieldColumn,
    		@RequestParam(required = true) String option) {
    	
     	logger.info("getMysqlTableDataByConvergeFilter connId = " + connId + "  databaseName="+databaseName + "  tableName="+tableName +"  fieldColumn="+fieldColumn);
       	
     	MysqlConnConf conn = getConnConfById(connId);
     	
    	List<Map> data = jdbcService.getXlsDataByConvergeFilter(conn,databaseName, tableName, fieldColumn,option);
    	
    	Map result = new HashMap<>();
    	result.put("data", data);
    	return new ResponseJson(200l,"success",result);
    }
	
}
