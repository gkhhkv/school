package com.example.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 注册请求 DTO。
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度需在2-50位之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 4, max = 255, message = "密码长度需在4-255位之间")
    private String password;

    private String phone;

    private String address;
}
