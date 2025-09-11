package com.leo.example.demo.service;

import com.leo.example.demo.dto.CreateOrderRequest;
import com.leo.example.demo.entities.Order;
import com.leo.example.demo.pubsub.OrderEventPublisher;
import com.leo.example.demo.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository repo;
    private final OrderEventPublisher publisher;

    public OrderService(OrderRepository repo, OrderEventPublisher publisher) {
        this.repo = repo; this.publisher = publisher;
    }

    @Transactional
    public Order create(CreateOrderRequest in) {
        var id = (in.orderId() == null || in.orderId().isBlank())
                ? UUID.randomUUID().toString()
                : in.orderId();

        var order = new Order(id, in.userId(), in.total(), "CREATED", Instant.now());
        repo.save(order);

        // event publishing
        try {
            var evt = new com.leo.example.demo.dto.OrderCreatedEvent(order.getId(), order.getUserId(), order.getTotal());
            publisher.publish(evt);
        } catch (Exception e) {
            throw new RuntimeException("FAILED to publish ORDER_CREATED", e);
        }

        return order;
    }
}
