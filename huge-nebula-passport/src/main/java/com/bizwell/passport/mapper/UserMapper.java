package com.bizwell.passport.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.bizwell.passport.bean.domain.User;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Repository
public interface UserMapper {
    
	int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
    
    public User selectByUserName(@Param("userName") String userName);
    
}