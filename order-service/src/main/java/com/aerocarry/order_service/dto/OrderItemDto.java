package com.aerocarry.order_service.dto;

import lombok.Data;

@Data
class OrderItemDto {
    private String sku;
    private Integer quantity;
}
