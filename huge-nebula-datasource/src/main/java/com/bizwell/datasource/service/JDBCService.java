package com.bizwell.datasource.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

/**
 * @author zhangjianjun
 * @date 2018年4月28日
 *
 */
@Service
public class JDBCService {

	@Autowired
	private DataSource dataSource;

	public void executeSql(String sql)  {
		
		Connection connection = DataSourceUtils.getConnection(dataSource);
		PreparedStatement preparedStatement = null;
//		Statement preparedStatement = null;
	    ResultSet resultSet = null;
	    // 创建Statement对象
        try {
			preparedStatement = connection.prepareStatement(sql);
			// 执行sql
			preparedStatement.execute();

			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	     
	}

}
