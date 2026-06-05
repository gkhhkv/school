package com.example.ecommerce.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.ecommerce.common.ResultCode;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.exception.BusinessException;
import com.example.ecommerce.repository.ProductMapper;
import com.example.ecommerce.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 商品服务实现。
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public Product createProduct(Product product) {
        product.setProductId(null);
        productMapper.insert(product);
        return product;
    }

    @Override
    public IPage<Product> queryProducts(int pageNum, int pageSize, String keyword) {
        Page<Product> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        if (keyword != null && !keyword.isBlank()) {
            wrapper.like(Product::getName, keyword);
        }
        wrapper.orderByDesc(Product::getCreatedAt);
        return productMapper.selectPage(page, wrapper);
    }

    @Override
    public Product getProductById(Long productId) {
        Product product = productMapper.selectById(productId);
        if (product == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "商品不存在");
        }
        return product;
    }

    @Override
    public Product updateProduct(Long productId, Product product) {
        Product existing = getProductById(productId);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStock(product.getStock());
        existing.setImage(product.getImage());
        productMapper.updateById(existing);
        return existing;
    }

    @Override
    public void deleteProduct(Long productId) {
        getProductById(productId);
        productMapper.deleteById(productId);
    }
}
