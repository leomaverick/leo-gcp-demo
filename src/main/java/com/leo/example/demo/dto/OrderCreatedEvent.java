package com.leo.example.demo.dto;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        String orderId,
        String userId,
        BigDecimal total
) {}
