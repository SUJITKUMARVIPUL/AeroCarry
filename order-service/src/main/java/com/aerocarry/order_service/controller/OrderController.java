package com.aerocarry.order_service.controller;

import com.aerocarry.order_service.dto.OrderRequest;
import com.aerocarry.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String placeOrder(
            @RequestBody OrderRequest orderRequest,
            @RequestHeader("LoggedInUser") String userEmail) {

        return orderService.placeOrder(orderRequest, userEmail);
    }
}
