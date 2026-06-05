package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.domain.Product;
import com.example.ecommerce.dto.ProductCreateRequest;
import com.example.ecommerce.dto.ProductUpdateRequest;
import com.example.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 商品控制器。
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "商品管理", description = "商品增删改查接口")
public class ProductController {

    private final ProductService productService;

    /**
     * 创建商品。
     *
     * @param request 创建请求
     * @return 创建的商品
     */
    @PostMapping
    @Operation(summary = "创建商品")
    public Result<Product> createProduct(@Valid @RequestBody ProductCreateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(request.getImage());
        return Result.success(productService.createProduct(product));
    }

    /**
     * 分页查询商品。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param keyword  搜索关键词
     * @return 分页结果
     */
    @GetMapping
    @Operation(summary = "分页查询商品")
    public Result<IPage<Product>> queryProducts(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String keyword) {
        return Result.success(productService.queryProducts(pageNum, pageSize, keyword));
    }

    /**
     * 查询商品详情。
     *
     * @param id 商品 ID
     * @return 商品
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询商品详情")
    public Result<Product> getProduct(@PathVariable Long id) {
        return Result.success(productService.getProductById(id));
    }

    /**
     * 更新商品。
     *
     * @param id      商品 ID
     * @param request 更新请求
     * @return 更新后的商品
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新商品")
    public Result<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImage(request.getImage());
        return Result.success(productService.updateProduct(id, product));
    }

    /**
     * 删除商品。
     *
     * @param id 商品 ID
     * @return 空结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品")
    public Result<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return Result.success(null);
    }
}
