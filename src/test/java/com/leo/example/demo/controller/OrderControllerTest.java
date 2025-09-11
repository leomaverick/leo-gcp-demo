package com.leo.example.demo.controller;

import com.leo.example.demo.dto.CreateOrderRequest;
import com.leo.example.demo.entities.Order;
import com.leo.example.demo.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderControllerTest {

    @Test
    void create_shouldDelegateToServiceAndReturnSameOrder() {
        OrderService service = Mockito.mock(OrderService.class);
        OrderController controller = new OrderController(service);

        CreateOrderRequest req = Mockito.mock(CreateOrderRequest.class);
        Order expected = Mockito.mock(Order.class);

        when(service.create(req)).thenReturn(expected);

        Order result = controller.create(req);

        assertSame(expected, result, "Controller must return the same Order produced by the service");
        verify(service, times(1)).create(req);
        verifyNoMoreInteractions(service);
    }

    @Test
    void create_shouldPropagateExceptionFromService() {
        OrderService service = Mockito.mock(OrderService.class);
        OrderController controller = new OrderController(service);

        CreateOrderRequest req = Mockito.mock(CreateOrderRequest.class);
        RuntimeException boom = new IllegalStateException("publishing failed");

        when(service.create(req)).thenThrow(boom);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> controller.create(req),
                "Controller should propagate service exceptions"
        );
        assertSame(boom, ex, "It should be the same exception instance from the service");

        verify(service, times(1)).create(req);
        verifyNoMoreInteractions(service);
    }

    @Test
    void create_shouldDelegateIndependentlyForMultipleCalls() {
        OrderService service = Mockito.mock(OrderService.class);
        OrderController controller = new OrderController(service);

        CreateOrderRequest req1 = Mockito.mock(CreateOrderRequest.class);
        CreateOrderRequest req2 = Mockito.mock(CreateOrderRequest.class);
        Order order1 = Mockito.mock(Order.class);
        Order order2 = Mockito.mock(Order.class);

        when(service.create(req1)).thenReturn(order1);
        when(service.create(req2)).thenReturn(order2);

        Order res1 = controller.create(req1);
        Order res2 = controller.create(req2);

        assertSame(order1, res1);
        assertSame(order2, res2);

        InOrder inOrder = inOrder(service);
        inOrder.verify(service).create(req1);
        inOrder.verify(service).create(req2);
        verifyNoMoreInteractions(service);
    }
}
