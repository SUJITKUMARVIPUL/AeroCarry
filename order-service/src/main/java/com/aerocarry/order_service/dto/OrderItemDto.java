package com.aerocarry.order_service.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private String sku;
    private Integer quantity;
}
