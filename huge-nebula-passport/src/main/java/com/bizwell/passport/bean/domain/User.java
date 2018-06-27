package com.bizwell.passport.bean.domain;

import java.util.Date;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
// 用户信息实体类
public class User {
	
    private Integer id;

    private String userName;

    private String password;

    private String companyName;

    private String telephone;

    private Date createTime;

    private Date updateTime;

    private Boolean isDel;

    private String reserved1;

    private String reserved2;

    private String reserved3;

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
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone == null ? null : telephone.trim();
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

    public Boolean getIsDel() {
        return isDel;
    }

    public void setIsDel(Boolean isDel) {
        this.isDel = isDel;
    }

    public String getReserved1() {
        return reserved1;
    }

    public void setReserved1(String reserved1) {
        this.reserved1 = reserved1 == null ? null : reserved1.trim();
    }

    public String getReserved2() {
        return reserved2;
    }

    public void setReserved2(String reserved2) {
        this.reserved2 = reserved2 == null ? null : reserved2.trim();
    }

    public String getReserved3() {
        return reserved3;
    }

    public void setReserved3(String reserved3) {
        this.reserved3 = reserved3 == null ? null : reserved3.trim();
    }

	@Override
	public String toString() {
		return String.format(
				"User [id=%s, userName=%s, password=%s, companyName=%s, telephone=%s, createTime=%s, updateTime=%s, isDel=%s, reserved1=%s, reserved2=%s, reserved3=%s]",
				id, userName, password, companyName, telephone, createTime, updateTime, isDel, reserved1, reserved2,
				reserved3);
	}
    
}