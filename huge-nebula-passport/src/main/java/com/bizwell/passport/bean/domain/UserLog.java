package com.bizwell.passport.bean.domain;

import java.util.Date;

/**
 * @author liujian
 * @date 2018年4月26日
 *
 */
// 用户登陆日志
public class UserLog {
	
    private Integer id;

    private Integer userId;
    
    private String system;
    
    private Date createTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getSystem() {
		return system;
	}

	public void setSystem(String system) {
		this.system = system;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    
    
    
}