package com.health.controller;

import com.health.common.Result;
import com.health.dto.UserLoginDTO;
import com.health.dto.UserRegisterDTO;
import com.health.entity.User;
import com.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<String> register(@RequestBody UserRegisterDTO dto) {
        String msg = userService.register(dto);
        if ("注册成功".equals(msg)) {
            return Result.success(msg, null);
        }
        return Result.error(msg);
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody UserLoginDTO dto) {
        User user = userService.login(dto);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("token", "token-" + user.getId());

        return Result.success("登录成功", data);
    }
}