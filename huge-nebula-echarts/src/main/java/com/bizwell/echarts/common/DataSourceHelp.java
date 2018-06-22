package com.bizwell.echarts.common;

import java.sql.SQLException;

import javax.sql.DataSource;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * @author zhangjianjun
 * @date 2018年5月14日
 *
 */
// 配置连接池公共类
public class DataSourceHelp {
	
	//获取连接池
	public static DataSource getDataSource(DruidProperties properties) {
		
		DruidDataSource dataSource = new DruidDataSource();
		dataSource.setUrl(properties.getUrl());
		dataSource.setUsername(properties.getUserName());
		dataSource.setPassword(properties.getPassword());
		dataSource.setDriverClassName(properties.getDriverClass());
		if (properties.getInitialSize() > 0) {
			dataSource.setInitialSize(properties.getInitialSize());			
		}
		if (properties.getMinIdle() > 0) {
			dataSource.setMinIdle(properties.getMinIdle());			
		}
		if (properties.getMaxActive() > 0) {
			dataSource.setMaxActive(properties.getMaxActive());			
		}
		dataSource.setTestOnBorrow(properties.getTestOnBorrow());
		
		try {
			dataSource.init();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return dataSource;
	}

}
