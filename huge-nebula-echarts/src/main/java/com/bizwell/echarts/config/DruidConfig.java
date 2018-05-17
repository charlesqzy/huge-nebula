package com.bizwell.echarts.config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.alibaba.druid.pool.DruidDataSource;
import com.bizwell.echarts.common.DruidProperties;

/**
 * @author zhangjianjun
 * @date 2018年4月28日
 *
 */
@Configuration
@EnableConfigurationProperties(DruidProperties.class)
@EnableTransactionManagement
public class DruidConfig {
	
	@Autowired
	private DruidProperties properties;
	
	@Bean(name = "dataSource")
	@Primary
	public DataSource dataSource() {
		
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
