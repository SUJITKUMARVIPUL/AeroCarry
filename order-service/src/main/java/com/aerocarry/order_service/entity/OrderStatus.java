package com.aerocarry.order_service.entity;

public enum OrderStatus {
    PENDING,        // Waiting for Payment
    CONFIRMED,      // Payment Success
    FAILED,         // Payment Failed / Out of Stock
    SHIPPED,
    DELIVERED
}
