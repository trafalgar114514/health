package com.health.controller;

import com.health.common.Result;
import com.health.dto.LoginDTO;
import com.health.service.UserService;
import com.health.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody LoginDTO dto) {
        return Result.success(userService.login(dto));
    }

    @GetMapping("/profile")
    public Result<UserVO> profile() {
        return Result.success(userService.getProfile());
    }
}