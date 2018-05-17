package com.bizwell.echarts.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import com.bizwell.echarts.bean.dto.QueryParam;

/**
 * @author zhangjianjun
 * @date 2018年4月28日
 *
 */
@Service
public class JDBCServiceImpl implements JDBCService {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public void getUser(QueryParam param) throws Exception {
		
		Connection connection = DataSourceUtils.getConnection(dataSource);
		PreparedStatement preparedStatement = null;
//		Statement preparedStatement = null;
	    ResultSet resultSet = null;
	    // 创建Statement对象
        String sql = "select * from gb_user";
        preparedStatement = connection.prepareStatement(sql);

        // 执行sql
        resultSet = preparedStatement.executeQuery(sql);

        // 遍历结果集数据
        while (resultSet.next()) {
        	
        	String latitude = resultSet.getString(param.getLatitude());
        	String[] value1 = param.getValue1();
        	for (int i = 0; i < value1.length; i++) {
        		BigDecimal bigDecimal = resultSet.getBigDecimal(value1[i]);
			}
        	
            System.out.println("userName = " + resultSet.getString("user_name"));
        }
        connection.close();
	     
	}

}
