package com.example.ecommerce.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper。
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
