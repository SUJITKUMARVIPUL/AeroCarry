package com.aerocarry.payment_service.event;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCompletedEvent {
    private String orderNumber;
    private String paymentStatus; // SUCCESS or FAILED
}
