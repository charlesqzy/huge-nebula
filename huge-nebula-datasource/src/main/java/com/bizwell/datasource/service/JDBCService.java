package com.bizwell.datasource.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.MysqlTableConf;

/**
 * @author zhangjianjun
 * @date 2018年4月28日
 *
 */
@Service
public class JDBCService {
	
	private Connection conn = null;
	private PreparedStatement ps = null;
	private ResultSet rs = null;
	
	@Autowired
	private DataSource dataSource;
	
	private static Logger logger = LoggerFactory.getLogger(JDBCService.class);

	
	
	public boolean executeSql(String sql)  {
		
		boolean flag = true;
		Connection connection = DataSourceUtils.getConnection(dataSource);
		PreparedStatement preparedStatement = null;
        try {
			preparedStatement = connection.prepareStatement(sql);
			// 执行sql
			preparedStatement.execute();
			connection.close();
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}
        return flag;
	}

	
	
	public boolean executeSql(String[] sqls)  {
		boolean flag = true;
		Connection connection = DataSourceUtils.getConnection(dataSource);
		PreparedStatement preparedStatement = null;
        try {
        	for(String sql : sqls){
				preparedStatement = connection.prepareStatement(sql);
				preparedStatement.execute();
	        }
			connection.close();
		} catch (SQLException e) {
			flag = false;
			e.printStackTrace();
		}
        return flag;
	}
	
	
	public boolean testMysqlConn(String dbUrl,String username,String password){
		boolean flag = false;
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			if (!conn.isClosed()){
				flag=true;
				logger.info(String.format("conection to %s successfully.", dbUrl));
			}
		} catch (Exception e) {
			flag=false;
			e.printStackTrace();
		} finally {
			try {
				if(null!=conn)
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}	
		}
		return flag;
	}
	
	/*
	public List<DatabaseInfo> getDatabaseTree(String dbUrl,String username,String password){
		List<DatabaseInfo> list = new ArrayList<DatabaseInfo>();
		DatabaseInfo databaseInfo;
		String databaseName= "";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			ps = conn.prepareStatement("SELECT DISTINCT TABLE_SCHEMA FROM information_schema.TABLES");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				databaseName = rs.getString(1);
				
				databaseInfo = new DatabaseInfo();				
				databaseInfo.setDatabaseName(databaseName);
				
				ps2 = conn.prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?");
				ps2.setString(1, databaseName);
				rs2 = ps2.executeQuery();
				
				while(rs){
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		
		return list;
	}
	*/
	
	public List<String> showDatabases(String dbUrl,String username,String password){
		List<String> list = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			ps = conn.prepareStatement("show databases");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}
	
	
	public List<MysqlTableConf> showTables(Integer connId,String dbUrl,String username,String password,String databaseName,Integer userId){
		List<MysqlTableConf> list = new ArrayList<MysqlTableConf>();
		MysqlTableConf mysqlTableConf = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
		
			ps = conn.prepareStatement("SELECT TABLE_SCHEMA,TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?");
			ps.setString(1, databaseName);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				mysqlTableConf = new MysqlTableConf();
				mysqlTableConf.setConnId(connId);
				mysqlTableConf.setDatabaseName(databaseName);
				mysqlTableConf.setTableName(rs.getString("TABLE_NAME"));
				mysqlTableConf.setUserId(userId);
				list.add(mysqlTableConf);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}
	
	

	private void closeDB() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}
}
