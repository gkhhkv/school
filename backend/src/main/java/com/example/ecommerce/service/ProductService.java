package com.example.ecommerce.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ecommerce.domain.Product;

/**
 * 商品服务接口。
 */
public interface ProductService {

    /**
     * 创建商品。
     *
     * @param product 商品信息
     * @return 创建的商品
     */
    Product createProduct(Product product);

    /**
     * 分页查询商品。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param category 分类筛选（可选）
     * @param keyword  搜索关键词（可选，匹配名称）
     * @return 分页结果
     */
    IPage<Product> queryProducts(int pageNum, int pageSize, String category, String keyword);

    /**
     * 根据 ID 查询商品。
     *
     * @param productId 商品 ID
     * @return 商品
     */
    Product getProductById(Long productId);

    /**
     * 更新商品信息。
     *
     * @param productId 商品 ID
     * @param product   新商品信息
     * @return 更新后的商品
     */
    Product updateProduct(Long productId, Product product);

    /**
     * 删除商品。
     *
     * @param productId 商品 ID
     */
    void deleteProduct(Long productId);
}
