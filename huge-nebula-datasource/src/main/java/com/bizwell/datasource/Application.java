package com.bizwell.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author liujian
 * @date 2018年2月6日
 *
 */
@SpringBootApplication
		//(exclude=DataSourceAutoConfiguration.class)
//@MapperScan(value = "com.bizwell.datasource.mapper")
public class Application {
	
	public static void main(String[] args) {
		
		SpringApplication.run(Application.class, args);
		System.out.println("start program ......");
	}
	
}
