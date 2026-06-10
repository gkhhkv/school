package com.example.ecommerce.service;

import com.example.ecommerce.dto.LoginRequest;

import java.util.Map;

/**
 * 用户服务接口。
 */
public interface UserService {

    /**
     * 用户登录。
     *
     * @param request 登录请求
     * @return token + userId + username
     * @throws com.example.ecommerce.exception.BusinessException 用户名或密码错误
     */
    Map<String, Object> login(LoginRequest request);
}
