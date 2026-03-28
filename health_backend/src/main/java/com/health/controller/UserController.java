package com.health.controller;

import com.health.common.Result;
import com.health.dto.LoginDTO;
import com.health.dto.RegisterDTO;
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

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO dto) {
        userService.register(dto);
        return Result.success(null);
    }

    @GetMapping("/profile")
    public Result<UserVO> profile() {
        return Result.success(userService.getProfile());
    }
}
