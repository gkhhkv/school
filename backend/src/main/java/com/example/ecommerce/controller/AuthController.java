package com.example.ecommerce.controller;

import com.example.ecommerce.common.Result;
import com.example.ecommerce.domain.User;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.RegisterRequest;
import com.example.ecommerce.dto.UserProfileUpdateRequest;
import com.example.ecommerce.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 认证控制器。
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "用户认证", description = "登录注册接口")
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录。
     *
     * @param request 登录请求
     * @return token + 用户信息
     */
    @PostMapping("/login")
    @Operation(summary = "用户登录")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginRequest request) {
        Map<String, Object> loginResult = userService.login(request);
        return Result.success(loginResult);
    }

    /**
     * 用户注册。
     *
     * @param request 注册请求
     * @return token + 用户信息（注册成功自动登录）
     */
    @PostMapping("/register")
    @Operation(summary = "用户注册")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterRequest request) {
        Map<String, Object> registerResult = userService.register(request);
        return Result.success(registerResult);
    }

    /**
     * 获取当前用户资料。
     *
     * @param httpReq HTTP 请求
     * @return 用户信息
     */
    @GetMapping("/profile")
    @Operation(summary = "获取用户资料")
    public Result<User> getProfile(HttpServletRequest httpReq) {
        Long userId = (Long) httpReq.getAttribute("userId");
        User user = userService.getProfile(userId);
        user.setPassword(null);
        return Result.success(user);
    }

    /**
     * 更新当前用户资料。
     *
     * @param request 资料更新请求
     * @param httpReq HTTP 请求
     * @return 空结果
     */
    @PutMapping("/profile")
    @Operation(summary = "更新用户资料")
    public Result<Void> updateProfile(@RequestBody UserProfileUpdateRequest request,
                                      HttpServletRequest httpReq) {
        Long userId = (Long) httpReq.getAttribute("userId");
        userService.updateProfile(userId, request);
        return Result.success(null);
    }
}
