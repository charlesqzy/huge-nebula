package com.bizwell.datasource.bean.vo;

import java.util.Date;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public class UserVo {
	
	private Integer id;

    private String userName;
    
    private String companyName;

    private String telephone;
    
    private String ticketCode;

    private Date createTime;

    private Date updateTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getTicketCode() {
		return ticketCode;
	}

	public void setTicketCode(String ticketCode) {
		this.ticketCode = ticketCode;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	public String toString() {
		return String.format(
				"UserVo [id=%s, userName=%s, companyName=%s, telephone=%s, ticketCode=%s, createTime=%s, updateTime=%s]",
				id, userName, companyName, telephone, ticketCode, createTime, updateTime);
	}

}
