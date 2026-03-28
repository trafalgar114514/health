package com.health.service;

import com.health.dto.UserLoginDTO;
import com.health.dto.UserRegisterDTO;
import com.health.entity.User;

public interface UserService {
    String register(UserRegisterDTO dto);

    User login(UserLoginDTO dto);
}