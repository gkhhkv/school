package com.example.ecommerce.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.Cart;
import org.apache.ibatis.annotations.Mapper;

/**
 * 购物车 Mapper。
 */
@Mapper
public interface CartMapper extends BaseMapper<Cart> {
}
