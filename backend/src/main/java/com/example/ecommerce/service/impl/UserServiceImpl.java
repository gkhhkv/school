package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.JwtUtil;
import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.repository.UserMapper;
import com.example.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户服务实现。
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    public Map<String, Object> login(LoginRequest request) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, request.getUsername()));
        if (user == null) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        if (!request.getPassword().equals(user.getPassword())) {
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        String token = JwtUtil.generateToken(user.getUserId(), user.getUsername());
        return Map.of("token", token, "userId", user.getUserId(), "username", user.getUsername());
    }
}
