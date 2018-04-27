package com.bizwell.passport.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bizwell.passport.bean.domain.User;
import com.bizwell.passport.bean.vo.UserVo;
import com.bizwell.passport.common.AESCodec;
import com.bizwell.passport.common.Constants;
import com.bizwell.passport.exception.PassportException;
import com.bizwell.passport.exception.ResponseCode;
import com.bizwell.passport.mapper.UserMapper;
import com.bizwell.passport.service.UserService;

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
