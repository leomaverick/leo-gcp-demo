package com.leo.example.demo.dto;

import java.math.BigDecimal;

public record CreateOrderRequest(
        String orderId,
        String userId,
        BigDecimal total
) {}
