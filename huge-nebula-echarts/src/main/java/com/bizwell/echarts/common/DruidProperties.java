package com.bizwell.echarts.common;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// Druid连接池配置所需参数
@ConfigurationProperties(prefix = "druid.report")
public class DruidProperties {
	
	private String url;
	
    private String userName;
    
    private String password;
    
    private String driverClass;

    private Integer maxActive;
    
    private Integer minIdle;
    
    private Integer initialSize;
    
    private Boolean testOnBorrow;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public Integer getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(Integer maxActive) {
		this.maxActive = maxActive;
	}

	public Integer getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}

	public Integer getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(Integer initialSize) {
		this.initialSize = initialSize;
	}

	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	@Override
	public String toString() {
		return "DruidProperties [url=" + url + ", userName=" + userName + ", password=" + password + ", driverClass="
				+ driverClass + ", maxActive=" + maxActive + ", minIdle=" + minIdle + ", initialSize=" + initialSize
				+ ", testOnBorrow=" + testOnBorrow + "]";
	}
	
}
