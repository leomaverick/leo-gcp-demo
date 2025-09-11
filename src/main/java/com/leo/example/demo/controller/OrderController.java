package com.leo.example.demo.controller;

import com.leo.example.demo.dto.CreateOrderRequest;
import com.leo.example.demo.entities.Order;
import com.leo.example.demo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Order create(@RequestBody CreateOrderRequest in) {
        return service.create(in);
    }
}

