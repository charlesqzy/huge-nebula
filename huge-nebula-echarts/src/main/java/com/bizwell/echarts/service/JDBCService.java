package com.bizwell.echarts.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.domain.SheetMetaData;
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

	public boolean executeSql(String sql) {

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

	public boolean executeSql(String[] sqls) {
		boolean flag = true;
		Connection connection = DataSourceUtils.getConnection(dataSource);
		PreparedStatement preparedStatement = null;
		try {
			for (String sql : sqls) {
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




	public List<String> showDatabases(String dbUrl, String username, String password) {
		List<String> list = new ArrayList<String>();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			ps = conn.prepareStatement("show databases");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}

	
	

	public List<Map<String,Object>> getMysqlTableData(String dbUrl, String username,String password, String sql) {
		Map row = null;
		List<Map<String,Object>> sheetList= new ArrayList();
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			
			while(rs.next()){
				row = new java.util.TreeMap<>();
				for (int i = 1; i <= columns; i++) {
					String columName = metaData.getColumnLabel(i);
					row.put(columName, rs.getString(columName));
				}
				sheetList.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
    	return sheetList;
	}
	
	
	
	
	public Integer getMysqlTableDataCount(String dbUrl, String username,String password, String sql) {
		int cnt=0;
		
		sql ="select count(1) from ("+sql+") t  ";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while(rs.next()){
				cnt=rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
    	return cnt;
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

	public SheetMetaData getMysqlTableMetaData(String dbUrl, String username, String password, String databaseName,
			String tableName, String fieldColumn) {

		SheetMetaData sheetMetadata =null;
		
		String sql = "SELECT TABLE_NAME tableName,COLUMN_NAME fieldColumn,COLUMN_COMMENT fieldNameNew, "
				+ "CASE WHEN DATA_TYPE IN('bigint','int','tinyint','decimal','double','float','mediumint','smallint') THEN 1 "
				+ " 	  WHEN DATA_TYPE IN('varchar','longtext','mediumtext','text','bit','char') THEN 2 "
				+ " 	  WHEN DATA_TYPE IN('datetime','timestamp','time','date') THEN 3 "
				+ "END fieldType FROM information_schema.COLUMNS "
				+ "WHERE TABLE_SCHEMA =? AND TABLE_NAME=? AND COLUMN_NAME=? ORDER BY fieldType DESC";
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			ps = conn.prepareStatement(sql);
			ps.setString(1, databaseName);
			ps.setString(2, tableName);
			ps.setString(3, fieldColumn);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sheetMetadata = new SheetMetaData();
				sheetMetadata.setTableName(rs.getString("tableName"));
				sheetMetadata.setFieldColumn(rs.getString("fieldColumn"));
				sheetMetadata.setFieldNameNew(StringUtils.isEmpty(rs.getString("fieldNameNew"))?rs.getString("fieldColumn"):rs.getString("fieldNameNew"));
				sheetMetadata.setFieldType(rs.getInt("fieldType"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return sheetMetadata;
	}

}
