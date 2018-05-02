package com.bizwell.datasource.mapper;


import com.bizwell.datasource.bean.User;
import org.apache.ibatis.annotations.Param;

/**
 * 后台用户管理
 * Created by liujian on 2017/10/9.
 */
public interface UserMapper {
    User check(@Param("username") String username, @Param("password") String password);
}