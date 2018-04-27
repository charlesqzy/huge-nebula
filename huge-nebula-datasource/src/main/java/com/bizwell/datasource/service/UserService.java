package com.bizwell.datasource.service;

import com.bizwell.datasource.bean.domain.User;
import com.bizwell.datasource.bean.vo.UserVo;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
public interface UserService {
	
	public UserVo login(String userName, String password) throws Exception;
	
	public User getUser(String userName);
	
	public void insertSelective(String userName, String password, String companyName, String telephone) throws Exception;

}
