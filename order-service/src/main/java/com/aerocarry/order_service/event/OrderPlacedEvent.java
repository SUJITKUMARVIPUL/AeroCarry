package com.aerocarry.order_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderPlacedEvent {
    private String orderNumber;
    private String userEmail;
    // We can add more fields later, like totalAmount or a list of SKUs for inventory deduction
}
