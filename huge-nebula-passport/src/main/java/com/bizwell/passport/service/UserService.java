package com.bizwell.passport.service;

import com.bizwell.passport.bean.domain.User;
import com.bizwell.passport.bean.vo.UserVo;

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
