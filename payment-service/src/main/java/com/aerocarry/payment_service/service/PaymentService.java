package com.aerocarry.payment_service.service;

import com.aerocarry.payment_service.entity.PaymentStatus;
import com.aerocarry.payment_service.entity.PaymentTransaction;
import com.aerocarry.payment_service.event.OrderPlacedEvent;
import com.aerocarry.payment_service.event.PaymentCompletedEvent;
import com.aerocarry.payment_service.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "orderTopic", groupId = "payment-group")
    public void consumeOrderEvent(OrderPlacedEvent event) {
        log.info("Received Order Event for Order Number: {}", event.getOrderNumber());

        // 1. Create a Pending Transaction Record
        PaymentTransaction transaction = PaymentTransaction.builder()
                .orderNumber(event.getOrderNumber())
                .status(PaymentStatus.PENDING)
                // The amount would typically be passed in the event or fetched via OpenFeign
                .build();

        paymentRepository.save(transaction);

        try {
            // 2. Simulate Third-Party Payment Gateway Call (Stripe/Razorpay)
            log.info("Initiating payment gateway call for Order: {}", event.getOrderNumber());
            processPaymentGateway(transaction);

            // 3. Update status on success
            transaction.setStatus(PaymentStatus.SUCCESS);
            transaction.setTransactionId("TXN-" + System.currentTimeMillis()); // Mock ID
            paymentRepository.save(transaction);

            // 4. Publish Success Event (Product Service will listen to this to deduct inventory)
            kafkaTemplate.send("paymentTopic", new PaymentCompletedEvent(event.getOrderNumber(), "SUCCESS"));
            log.info("Payment SUCCESS for Order: {}", event.getOrderNumber());

        } catch (Exception e) {
            // 5. Handle Failure and trigger a Rollback in the Saga
            transaction.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(transaction);

            kafkaTemplate.send("paymentTopic", new PaymentCompletedEvent(event.getOrderNumber(), "FAILED"));
            log.error("Payment FAILED for Order: {}. Initiating rollback.", event.getOrderNumber());
        }
    }

    private void processPaymentGateway(PaymentTransaction transaction) {
        // Here we will eventually add the Razorpay/Stripe SDK logic.
        // For now, we simulate a successful network call.
        try {
            Thread.sleep(2000); // Simulate network latency
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
