package com.bizwell.passport.bean.domain;

import java.io.Serializable;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 生成的ticket实体类
public class Ticket implements Serializable {
	
	private static final long serialVersionUID = -5554497147788660390L;
	
	private String ticket;
	
	private Integer userId;
	
	private String userIp;
	
	private String userName;
	
	private long genteateTime;

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public long getGenteateTime() {
		return genteateTime;
	}

	public void setGenteateTime(long genteateTime) {
		this.genteateTime = genteateTime;
	}

	public String getUserIp() {
		return userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return String.format("Ticket [ticket=%s, userId=%s, userIp=%s, userName=%s, genteateTime=%s]", ticket, userId,
				userIp, userName, genteateTime);
	}
	
}
