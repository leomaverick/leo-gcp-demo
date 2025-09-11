package com.leo.example.demo.service;

import com.leo.example.demo.dto.CreateOrderRequest;
import com.leo.example.demo.dto.OrderCreatedEvent;
import com.leo.example.demo.entities.Order;
import com.leo.example.demo.pubsub.OrderEventPublisher;
import com.leo.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    private OrderRepository repo;
    private OrderEventPublisher publisher;
    private OrderService service;

    @BeforeEach
    void setUp() {
        repo = mock(OrderRepository.class);
        publisher = mock(OrderEventPublisher.class);
        service = new OrderService(repo, publisher);

        when(repo.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void create_withExplicitId_shouldSavePublishAndReturnOrder() throws Exception {
        var req = new CreateOrderRequest("T-9000", "U-1", new BigDecimal("42.50"));

        Order out = service.create(req);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(repo, times(1)).save(orderCaptor.capture());
        Order saved = orderCaptor.getValue();

        assertEquals("T-9000", saved.getId());
        assertEquals("U-1", saved.getUserId());
        assertEquals(new BigDecimal("42.50"), saved.getTotal());
        assertEquals("CREATED", saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        ArgumentCaptor<OrderCreatedEvent> evtCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(publisher, times(1)).publish(evtCaptor.capture());
        OrderCreatedEvent evt = evtCaptor.getValue();
        assertEquals("T-9000", evt.orderId());
        assertEquals("U-1", evt.userId());
        assertEquals(new BigDecimal("42.50"), evt.total());

        assertEquals(saved.getId(), out.getId());
        assertEquals(saved.getUserId(), out.getUserId());
        assertEquals(saved.getTotal(), out.getTotal());

        verifyNoMoreInteractions(repo, publisher);
    }

    @Test
    void create_withoutId_shouldGenerateUuidSaveAndPublish() throws Exception {
        var req = new CreateOrderRequest("  ", "U-77", new BigDecimal("100.00"));

        Order out = service.create(req);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
        verify(repo).save(orderCaptor.capture());
        Order saved = orderCaptor.getValue();

        assertNotNull(saved.getId());
        assertFalse(saved.getId().isBlank());
        assertDoesNotThrow(() -> UUID.fromString(saved.getId()));

        assertEquals("U-77", saved.getUserId());
        assertEquals(new BigDecimal("100.00"), saved.getTotal());
        assertEquals("CREATED", saved.getStatus());
        assertNotNull(saved.getCreatedAt());

        // Publisher got a consistent event
        ArgumentCaptor<OrderCreatedEvent> evtCaptor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(publisher).publish(evtCaptor.capture());
        OrderCreatedEvent evt = evtCaptor.getValue();
        assertEquals(saved.getId(), evt.orderId());
        assertEquals("U-77", evt.userId());
        assertEquals(new BigDecimal("100.00"), evt.total());

        assertEquals(saved.getId(), out.getId());

        verifyNoMoreInteractions(repo, publisher);
    }

    @Test
    void create_whenPublisherFails_shouldWrapAndThrowAndStillHaveSaved() throws Exception {
        var req = new CreateOrderRequest("T-err", "U-2", new BigDecimal("9.99"));
        doThrow(new IllegalStateException("pubsub down")).when(publisher).publish(any());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(req));
        assertTrue(ex.getMessage().contains("FAILED to publish ORDER_CREATED"));
        assertNotNull(ex.getCause());
        assertEquals("pubsub down", ex.getCause().getMessage());

        verify(repo, times(1)).save(any(Order.class));
        verify(publisher, times(1)).publish(any());
        verifyNoMoreInteractions(repo, publisher);
    }

    @Test
    void create_whenRepositoryFails_shouldPropagateAndNotPublish() {
        var req = new CreateOrderRequest("T-db", "U-3", new BigDecimal("33.33"));
        when(repo.save(any(Order.class))).thenThrow(new RuntimeException("db failure"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> service.create(req));
        assertEquals("db failure", ex.getMessage());

        verify(repo, times(1)).save(any(Order.class));
        verifyNoInteractions(publisher);
        verifyNoMoreInteractions(repo);
    }
}
