package com.health.service.impl;

import com.health.dto.UserLoginDTO;
import com.health.dto.UserRegisterDTO;
import com.health.entity.User;
import com.health.mapper.UserMapper;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public String register(UserRegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            return "用户名不能为空";
        }
        if (dto.getPassword() == null || dto.getPassword().trim().isEmpty()) {
            return "密码不能为空";
        }

        User existUser = userMapper.findByUsername(dto.getUsername());
        if (existUser != null) {
            return "用户名已存在";
        }

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userMapper.insertUser(user);
        return "注册成功";
    }

    @Override
    public User login(UserLoginDTO dto) {
        if (dto.getUsername() == null || dto.getPassword() == null) {
            return null;
        }

        User user = userMapper.findByUsername(dto.getUsername().trim());
        if (user == null) {
            return null;
        }

        boolean match = passwordEncoder.matches(dto.getPassword(), user.getPassword());
        if (!match) {
            return null;
        }

        user.setPassword(null);
        return user;
    }
}