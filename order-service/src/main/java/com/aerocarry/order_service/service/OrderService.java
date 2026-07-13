package com.aerocarry.order_service.service;

import com.aerocarry.order_service.dto.OrderRequest;
import com.aerocarry.order_service.entity.Order;
import com.aerocarry.order_service.entity.OrderItem;
import com.aerocarry.order_service.entity.OrderStatus;
import com.aerocarry.order_service.event.OrderPlacedEvent;
import com.aerocarry.order_service.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(OrderRequest orderRequest, String userEmail) {

        // 1. Generate unique order ID
        String orderNumber = UUID.randomUUID().toString();

        // 2. Map DTO to Entity
        Order order = new Order();
        order.setOrderNumber(orderNumber);
        order.setUserEmail(userEmail);
        order.setStatus(OrderStatus.PENDING);
        // Note: In a real flow, we use OpenFeign to call Product Service to get the true price
        order.setTotalAmount(BigDecimal.ZERO);

        List<OrderItem> orderItems = orderRequest.getItems().stream()
                .map(dto -> {
                    OrderItem item = new OrderItem();
                    item.setSku(dto.getSku());
                    item.setQuantity(dto.getQuantity());
                    // item.setPrice(...) would be set after checking product service
                    item.setOrder(order);
                    return item;
                }).collect(Collectors.toList());

        order.setOrderItems(orderItems);

        // 3. Save to database
        orderRepository.save(order);
        log.info("Order {} is saved with status PENDING", order.getOrderNumber());

        // 4. Publish Event to Kafka for Saga Orchestration
        OrderPlacedEvent event = new OrderPlacedEvent(order.getOrderNumber(), userEmail);
        kafkaTemplate.send("orderTopic", event);
        log.info("OrderPlacedEvent sent to Kafka for Order: {}", order.getOrderNumber());

        return "Order Placed Successfully. Awaiting Payment Validation.";
    }
}
