package com.aerocarry.order_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class OrderRequest {
    private List<OrderItemDto> items;
}
