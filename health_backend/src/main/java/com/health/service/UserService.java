package com.health.service;

import com.health.dto.LoginDTO;
import com.health.vo.UserVO;

import java.util.Map;

public interface UserService {
    Map<String, String> login(LoginDTO dto);

    UserVO getProfile();
}