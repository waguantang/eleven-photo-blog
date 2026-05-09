package com.blog.service;

import com.blog.common.BizException;
import com.blog.common.Result;
import com.blog.dto.LoginRequest;
import com.blog.entity.User;
import com.blog.mapper.UserMapper;
import com.blog.utils.JwtUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public Result<Map<String, Object>> login(LoginRequest request) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BizException(401, "用户名或密码错误");
        }
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        return Result.success(data);
    }

    public Result<Void> logout(String token) {
        jwtUtils.invalidateToken(token);
        return Result.success();
    }

    public Result<Map<String, Object>> session(Long userId, String username) {
        Map<String, Object> data = new HashMap<>();
        data.put("loggedIn", true);
        data.put("username", username);
        data.put("userId", userId);
        return Result.success(data);
    }
}
