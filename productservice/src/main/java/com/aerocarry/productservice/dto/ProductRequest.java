package com.aerocarry.productservice.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class ProductRequest {
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;

    // IDs for the foreign key relationships
    private Long typeId;
    private Long categoryId;
    private Long sizeId;
    private Long colorId;
    private Set<Long> featureIds;
}