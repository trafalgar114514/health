package com.health.mapper;

import com.health.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("select id, username, password, nickname from sys_user where username = #{username}")
    User findByUsername(String username);
}