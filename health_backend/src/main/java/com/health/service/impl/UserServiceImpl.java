package com.health.service.impl;

import com.health.dto.LoginDTO;
import com.health.dto.RegisterDTO;
import com.health.entity.User;
import com.health.mapper.UserMapper;
import com.health.service.UserService;
import com.health.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private UserMapper userMapper;

    @Override
    public Map<String, String> login(LoginDTO dto) {
        User user = userMapper.findByUsername(dto.getUsername());
        if (user == null || !PASSWORD_ENCODER.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        Map<String, String> map = new HashMap<>();
        map.put("token", "test-token");
        return map;
    }

    @Override
    public void register(RegisterDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().trim().isEmpty()) {
            throw new RuntimeException("用户名不能为空");
        }
        if (dto.getPassword() == null || dto.getPassword().length() < 6) {
            throw new RuntimeException("密码长度不能小于6位");
        }

        User existed = userMapper.findByUsername(dto.getUsername().trim());
        if (existed != null) {
            throw new RuntimeException("用户名已存在");
        }

        User user = new User();
        user.setUsername(dto.getUsername().trim());
        user.setPassword(PASSWORD_ENCODER.encode(dto.getPassword()));
        user.setNickname((dto.getNickname() == null || dto.getNickname().trim().isEmpty())
                ? dto.getUsername().trim()
                : dto.getNickname().trim());
        userMapper.insert(user);
    }

    @Override
    public UserVO getProfile() {
        User user = userMapper.findByUsername("test");
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
