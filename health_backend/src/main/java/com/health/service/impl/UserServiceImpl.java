package com.health.service.impl;

import com.health.dto.LoginDTO;
import com.health.entity.User;
import com.health.mapper.UserMapper;
import com.health.service.UserService;
import com.health.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> login(LoginDTO dto) {
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null || !user.getPassword().equals(dto.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        Map<String, String> map = new HashMap<>();
        map.put("token", "test-token");
        return map;
    }

    @Override
    public UserVO getProfile() {
        User user = userMapper.findByUsername("test");
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}