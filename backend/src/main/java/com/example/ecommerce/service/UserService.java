package com.example.ecommerce.service;

import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserProfileUpdateRequest;

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

    /**
     * 用户注册。
     *
     * @param request 注册请求
     * @return token + userId + username（注册成功直接登录）
     * @throws com.example.ecommerce.exception.BusinessException 用户名已存在
     */
    Map<String, Object> register(RegisterRequest request);

    /**
     * 获取用户资料。
     *
     * @param userId 用户ID
     * @return 用户实体
     * @throws com.example.ecommerce.exception.BusinessException 用户不存在
     */
    User getProfile(Long userId);

    /**
     * 更新用户资料。
     *
     * @param userId  用户ID
     * @param request 更新请求
     */
    void updateProfile(Long userId, UserProfileUpdateRequest request);
}
