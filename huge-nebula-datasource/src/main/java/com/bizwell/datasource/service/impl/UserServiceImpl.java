package com.bizwell.datasource.service.impl;

import com.bizwell.datasource.common.AESCodec;
import com.bizwell.datasource.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bizwell.datasource.bean.domain.User;
import com.bizwell.datasource.bean.vo.UserVo;
import com.bizwell.datasource.common.Constants;
import com.bizwell.datasource.exception.PassportException;
import com.bizwell.datasource.exception.ResponseCode;
import com.bizwell.datasource.mapper.UserMapper;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public UserVo login(String userName, String password) throws Exception {

		User user = userMapper.selectByUserName(userName);
		if (user == null) {
			throw new PassportException(ResponseCode.PASSPORT_FAIL03.getCode(), ResponseCode.PASSPORT_FAIL03.getMessage());
		}
		
		String pwd = AESCodec.decrypt(user.getPassword(), Constants.AES_KEY);
		if (!password.equals(pwd)) {
			throw new PassportException(ResponseCode.PASSPORT_FAIL04.getCode(), ResponseCode.PASSPORT_FAIL04.getMessage());
		}
		
		UserVo userVo = new UserVo();
		BeanUtils.copyProperties(user, userVo);
		return userVo;
	}

	@Override
	public User getUser(String userName) {
		
		User user = userMapper.selectByUserName(userName);
		return user;
	}

	@Transactional
	@Override
	public void insertSelective(String userName, String password, String companyName, String telephone) throws Exception {

		String pwd = AESCodec.encrypt(password, Constants.AES_KEY);
		User user = new User();
		user.setUserName(userName);
		user.setPassword(pwd);
		user.setCompanyName(companyName);
		user.setTelephone(telephone);
		userMapper.insertSelective(user);
		
	}
	
}
