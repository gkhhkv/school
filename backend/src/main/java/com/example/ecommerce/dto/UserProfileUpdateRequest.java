package com.example.ecommerce.dto;

import lombok.Data;

/**
 * 用户资料更新请求。
 */
@Data
public class UserProfileUpdateRequest {

    private String phone;

    private String address;
}
