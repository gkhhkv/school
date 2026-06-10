package com.example.ecommerce.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ecommerce.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品 Mapper。
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 批量查询商品并加悲观锁，防止并发超卖。
     *
     * @param ids 商品 ID 列表
     * @return 商品列表
     */
    @Select("<script>"
            + "SELECT * FROM product WHERE product_id IN "
            + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach>"
            + " FOR UPDATE"
            + "</script>")
    List<Product> selectBatchIdsForUpdate(@Param("ids") List<Long> ids);
}
