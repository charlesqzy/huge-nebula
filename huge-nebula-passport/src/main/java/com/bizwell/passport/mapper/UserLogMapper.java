package com.bizwell.passport.mapper;

import org.springframework.stereotype.Repository;

import com.bizwell.passport.bean.domain.UserLog;

/**
 * @author zhangjianjun
 * @date 2018年4月26日
 *
 */
@Repository
public interface UserLogMapper {

    int insert(UserLog record);
    
}