package com.aerocarry.productservice.service;

import com.aerocarry.productservice.dto.ProductRequest;
import com.aerocarry.productservice.dto.ProductResponse;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    ProductResponse getProductBySku(String sku);
    List<ProductResponse> getAllProducts();
    ProductResponse createProduct(ProductRequest request);
    void deleteProduct(Long id);
    Page<ProductResponse> searchProducts(Long typeId, Long categoryId, Long sizeId,
                                         Long colorId, BigDecimal minPrice,
                                         BigDecimal maxPrice, int page, int size);

    ProductResponse updateProduct(Long id, ProductRequest request);
}