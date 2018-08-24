package com.bizwell.datasource.service;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
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
import java.util.TreeMap;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.bizwell.datasource.bean.MysqlConnConf;
import com.bizwell.datasource.bean.MysqlTableConf;
import com.bizwell.datasource.bean.SheetMetadata;
import com.bizwell.datasource.bean.XLSHaderType;
import com.bizwell.datasource.common.JsonUtils;

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

	@Autowired
	private MysqlTableConfService mysqlTableConfService;

	@Autowired
	private MysqlConnConfService mysqlConnConfService;

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

	public boolean testMysqlConn(String dbUrl, String username, String password) {
		boolean flag = false;
		Connection conn = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);
			if (!conn.isClosed()) {
				flag = true;
				logger.info(String.format("conection to %s successfully.", dbUrl));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		} finally {
			try {
				if (null != conn)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	/*
	 * public List<DatabaseInfo> getDatabaseTree(String dbUrl,String
	 * username,String password){ List<DatabaseInfo> list = new
	 * ArrayList<DatabaseInfo>(); DatabaseInfo databaseInfo; String
	 * databaseName= "";
	 * 
	 * try { Class.forName("com.mysql.jdbc.Driver"); conn =
	 * DriverManager.getConnection(dbUrl, username, password); ps = conn.
	 * prepareStatement("SELECT DISTINCT TABLE_SCHEMA FROM information_schema.TABLES"
	 * ); ResultSet rs = ps.executeQuery(); while(rs.next()){ databaseName =
	 * rs.getString(1);
	 * 
	 * databaseInfo = new DatabaseInfo();
	 * databaseInfo.setDatabaseName(databaseName);
	 * 
	 * ps2 = conn.
	 * prepareStatement("SELECT TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?"
	 * ); ps2.setString(1, databaseName); rs2 = ps2.executeQuery();
	 * 
	 * while(rs){ } } catch (Exception e) { e.printStackTrace(); } finally {
	 * closeDB(); }
	 * 
	 * return list; }
	 */

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

	public List<MysqlTableConf> showTables(Integer connId, String dbUrl, String username, String password,
			String databaseName, Integer userId) {
		List<MysqlTableConf> list = new ArrayList<MysqlTableConf>();
		MysqlTableConf mysqlTableConf = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dbUrl, username, password);

			ps = conn.prepareStatement(
					"SELECT TABLE_SCHEMA,TABLE_NAME FROM information_schema.TABLES WHERE TABLE_SCHEMA = ?");
			ps.setString(1, databaseName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
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

	/**
	 * 获取mysql表中的字段元数据
	 * 
	 * @param connConf
	 * @param databaseName
	 * @param tableName
	 * @return
	 */
	public List<SheetMetadata> getMetadataByMysqlTableName(MysqlConnConf connConf, String databaseName,
			String tableName) {
		List<SheetMetadata> list = new ArrayList<SheetMetadata>();
		SheetMetadata sheetMetadata = null;

		String sql = "SELECT TABLE_NAME tableName,COLUMN_NAME fieldColumn,COLUMN_COMMENT fieldNameNew, "
				+ "CASE WHEN DATA_TYPE IN('bigint','int','tinyint','decimal','double','float','mediumint','smallint') THEN 1 "
				+ " 	  WHEN DATA_TYPE IN('varchar','longtext','mediumtext','text','bit','char','enum') THEN 2 "
				+ " 	  WHEN DATA_TYPE IN('datetime','timestamp','time','date') THEN 3 "
				+ "END fieldType FROM information_schema.COLUMNS "
				+ "WHERE TABLE_SCHEMA =? AND TABLE_NAME=? ORDER BY fieldType DESC";

		// System.out.println(sql);

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connConf.getDbUrl(), connConf.getUsername(), connConf.getPassword());
			ps = conn.prepareStatement(sql);
			ps.setString(1, databaseName);
			ps.setString(2, tableName);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				sheetMetadata = new SheetMetadata();
				sheetMetadata.setTableName(rs.getString("tableName"));
				sheetMetadata.setFieldColumn(rs.getString("fieldColumn"));
				sheetMetadata.setFieldNameNew(StringUtils.isEmpty(rs.getString("fieldNameNew"))
						? rs.getString("fieldColumn") : rs.getString("fieldNameNew"));
				sheetMetadata.setFieldType(rs.getInt("fieldType"));
				list.add(sheetMetadata);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}

	public Map getMysqlTableData(MysqlConnConf connConf, String databaseName,
			String tableName,Integer pageNum,Integer pageSize) {
		Map row = null;
		List<Map<String,String>> sheetList= new ArrayList();
		XLSHaderType header = null;
		List<XLSHaderType> haderList = new ArrayList();
		
		int totalRows = 0; 


		String metadataSql = "SELECT TABLE_NAME tableName,COLUMN_NAME fieldColumn,COLUMN_COMMENT fieldNameNew, "
				+ "CASE WHEN DATA_TYPE IN('bigint','int','tinyint','decimal','double','float','mediumint','smallint') THEN 1 "
				+ " 	  WHEN DATA_TYPE IN('varchar','longtext','mediumtext','text','bit','char','enum') THEN 2 "
				+ " 	  WHEN DATA_TYPE IN('datetime','timestamp','time','date') THEN 3 "
				+ "END fieldType FROM information_schema.COLUMNS "
				+ "WHERE TABLE_SCHEMA =? AND TABLE_NAME=? ";
				//+ "ORDER BY fieldType DESC";
		
		String countSql = "select count(1) from "+databaseName+"."+ tableName;		
		String sql = "SELECT * from "+databaseName+"."+ tableName + " limit "+(pageNum-1)*pageSize+","+pageSize;
		

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connConf.getDbUrl(), connConf.getUsername(), connConf.getPassword());
			

			ps = conn.prepareStatement(metadataSql);
			ps.setString(1, databaseName);
			ps.setString(2, tableName);
			rs = ps.executeQuery();
			while(rs.next()){
				header = new XLSHaderType();
				header.setType(rs.getString("fieldType"));
				header.setLabel(StringUtils.isEmpty(rs.getString("fieldNameNew"))
						? rs.getString("fieldColumn") : rs.getString("fieldNameNew"));
				header.setProp(rs.getString("fieldColumn"));//
				haderList.add(header);
			}

			ps = conn.prepareStatement(countSql);
			rs = ps.executeQuery();
			while(rs.next()){
				totalRows=rs.getInt(1);
			}
		
		
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			while(rs.next()){
				row = new TreeMap<>();
				for (int i = 1; i <= columns; i++) {
					String columName = metaData.getColumnName(i);
					row.put(columName, rs.getString(i));
				}
				sheetList.add(row);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}

    	Map result = new HashMap<>();
    	result.put("sheet", sheetList);
    	result.put("header", haderList);
    	result.put("totalRows", totalRows);
    	result.put("pageNum", pageNum);
    	return result;
	}



	/**
	 * 字段类型 1,数字 2,文本 ,3 日期
	 * 
	 * @param type
	 * @return
	 */
	/*private static Integer getFieldType(int type) {
		int fieldType = 0;
		if (Types.INTEGER == type || Types.BIGINT == type || Types.TINYINT == type || Types.DECIMAL == type
				|| Types.DOUBLE == type || Types.FLOAT == type || Types.SMALLINT == type) {
			fieldType = 1;
		} else if (Types.DATE == type || Types.TIMESTAMP == type || Types.TIME == type || Types.DATE == type) {
			fieldType = 3;
		} else { // (Types.VARCHAR == type || Types.LONGVARCHAR== type ||
					// Types.LONGNVARCHAR == type || Types.CHAR == type )
			fieldType = 2;
		}
		return fieldType;
	}*/

	/**
	 * 获取mysql表的行和列
	 * 
	 * @param tableList
	 */
	public void setMysqlTableRowsClumns(List<MysqlTableConf> tableList) {

		for (MysqlTableConf tableConf : tableList) {
			MysqlConnConf entity = new MysqlConnConf();
			entity.setId(tableConf.getConnId());
			List<MysqlConnConf> connList = mysqlConnConfService.select(entity);

			Integer tableClumns = 0;
			Integer tableRows = 0;

			String countSql = "select count(1) from " + tableConf.getDatabaseName() + "." + tableConf.getTableName();
			String sql = "SELECT * from " + tableConf.getDatabaseName() + "." + tableConf.getTableName();
			try {
				Class.forName("com.mysql.jdbc.Driver");
				conn = DriverManager.getConnection(connList.get(0).getDbUrl(), connList.get(0).getUsername(),
						connList.get(0).getPassword());

				ps = conn.prepareStatement(countSql);
				rs = ps.executeQuery();
				while (rs.next()) {
					tableRows = rs.getInt(1);
				}

				ps = conn.prepareStatement(sql);
				rs = ps.executeQuery();
				ResultSetMetaData metaData = rs.getMetaData();
				tableClumns = metaData.getColumnCount();

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				closeDB();
			}

			tableConf.setTableClumns(tableClumns);
			tableConf.setTableRows(tableRows);
		}

	}

	public List<Map> getMysqlTableDataByFilter(MysqlConnConf connConf, String databaseName, String tableName,
			String fieldColumn) {
		Map<String, String> row = null;
		List<Map> list = new ArrayList();

		String sql = "select distinct " + fieldColumn + " from " + databaseName + "." + tableName;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connConf.getDbUrl(), connConf.getUsername(), connConf.getPassword());
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				row = new HashMap<String, String>();
				row.put("content", rs.getString(1));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}

	/**
	 * 指定列名查询mysql表中的数据 distinct
	 * 
	 * @param conn
	 * @param databaseName
	 * @param tableName
	 * @param option
	 * @return
	 */
	public List<Map> getMysqlTableDateByFilter(MysqlConnConf connConf, String databaseName, String tableName,
			String fieldColumn, String option) {
		Map<String, String> row = null;
		List<Map> list = new ArrayList();

		String sql = "select 1 ";

		if ("byDay".equals(option)) {
			sql = "SELECT DISTINCT DATE_FORMAT(" + fieldColumn + ",'%Y-%m-%d') AS DAY FROM " + databaseName + "."
					+ tableName + " ORDER BY " + fieldColumn + " DESC";
		} else if ("byMonth".equals(option)) {
			sql = "SELECT DISTINCT DATE_FORMAT(" + fieldColumn + ",'%Y-%m') AS DAY FROM " + databaseName + "."
					+ tableName + " ORDER BY " + fieldColumn + " DESC";
		} else if ("byQuarter".equals(option)) {
			sql = "SELECT CONCAT(CONCAT(DATE_FORMAT(" + fieldColumn + ",'%Y'),\"年\"),CONCAT(FLOOR((DATE_FORMAT("
					+ fieldColumn + ", '%m')+2)/3)),\"季度\") DAY FROM " + databaseName + "." + tableName
					+ " tv GROUP BY DAY ORDER BY DAY DESC";
		} else if ("byYear".equals(option)) {
			sql = "SELECT DISTINCT DATE_FORMAT(" + fieldColumn + ",'%Y') AS DAY FROM " + databaseName + "." + tableName
					+ " ORDER BY " + fieldColumn + " DESC";
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connConf.getDbUrl(), connConf.getUsername(), connConf.getPassword());
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				row = new HashMap<String, String>();
				row.put("DAY", rs.getString("DAY"));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}

	/**
	 * 获取mysql表中的指定数字列的最大值和最小值
	 * 
	 * @param tableName
	 * @param fieldColumn
	 * @return
	 */
	public List<Map> getMysqlTableDataByNumberFilter(MysqlConnConf connConf, String databaseName, String tableName,
			String fieldColumn) {
		Map<String, String> row = null;
		List<Map> list = new ArrayList();

		String sql = "SELECT MAX(" + fieldColumn + ") AS MAX,MIN(" + fieldColumn + ") AS MIN FROM " + databaseName + "."
				+ tableName;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connConf.getDbUrl(), connConf.getUsername(), connConf.getPassword());
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				row = new HashMap<String, String>();
				row.put("MAX", rs.getString("MAX"));
				row.put("MIN", rs.getString("MIN"));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}

	public List<Map> getXlsDataByConvergeFilter(MysqlConnConf connConf, String databaseName, String tableName,
			String fieldColumn, String option) {
		Map<String, String> row = null;
		List<Map> list = new ArrayList();

		String sql = "";

		if ("sum".equals(option)) {
			sql = "SELECT 0 AS MIN,SUM(" + fieldColumn + ") AS MAX FROM " + databaseName + "." + tableName;
		} else if ("count".equals(option)) {
			sql = "SELECT 0 AS MIN,count(" + fieldColumn + ") AS MAX FROM " + databaseName + "." + tableName;
		} else if ("countDistinct".equals(option)) {
			sql = "SELECT 0 AS MIN,count(distinct " + fieldColumn + ") AS MAX FROM " + databaseName + "." + tableName;
		} else if ("avg".equals(option)) {
			sql = "SELECT 0 AS MIN,avg(" + fieldColumn + ") AS MAX FROM " + databaseName + "." + tableName;
		} else if ("max".equals(option)) {
			sql = "SELECT max(" + fieldColumn + ") AS MAX FROM " + databaseName + "." + tableName;
		} else if ("min".equals(option)) {
			sql = "SELECT min(" + fieldColumn + ") AS MAX FROM " + databaseName + "." + tableName;
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(connConf.getDbUrl(), connConf.getUsername(), connConf.getPassword());
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				row = new HashMap<String, String>();
				row.put("MAX", rs.getString("MAX"));
				row.put("MIN", rs.getString("MIN"));
				list.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDB();
		}
		return list;
	}

	public static void main(String[] args) {
		JDBCService jdbc = new JDBCService();
		String dbUrl = "jdbc:mysql://10.10.102.201:3306/betty_bi?useUnicode=true&amp;characterEncoding=UTF-8";
		String username = "betty";
		String password = "betty#123";
		MysqlConnConf connConf = new MysqlConnConf();
		connConf.setDbUrl(dbUrl);
		connConf.setUsername(username);
		connConf.setPassword(password);

		String databaseName = "betty";
		String tableName = "dc_activity";
		Map m = jdbc.getMysqlTableData(connConf, databaseName, tableName, 1, 20);
		
		System.out.println(JsonUtils.toJson(m));
	}

	/*
	 * 
	 * SELECT TABLE_SCHEMA tableName, COLUMN_NAME fieldColumn, CASE WHEN
	 * DATA_TYPE
	 * IN('bigint','int','tinyint','decimal','double','float','mediumint','
	 * smallint') THEN 1 WHEN DATA_TYPE
	 * IN('varchar','longtext','mediumtext','text','bit','char') THEN 2 WHEN
	 * DATA_TYPE IN('datetime','timestamp','time','date') THEN 3 END fieldType
	 * FROM information_schema.COLUMNS WHERE TABLE_SCHEMA ='information_schema'
	 * AND TABLE_NAME='COLLATIONS' ORDER BY fieldType DESC
	 * 
	 */

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
