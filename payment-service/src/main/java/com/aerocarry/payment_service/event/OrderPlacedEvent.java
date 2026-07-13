package com.aerocarry.payment_service.event;

import lombok.Data;

@Data
public class OrderPlacedEvent {
    private String orderNumber;
    private String userEmail;
}
