package com.aerocarry.productservice.dto;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class ProductResponse {
    private String sku;
    private String name;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private String type;
    private String category;
    private String size;
    private String color;
    private List<String> features;
}