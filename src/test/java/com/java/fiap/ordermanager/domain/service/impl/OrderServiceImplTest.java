package com.java.fiap.ordermanager.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderItemForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.gateway.MsCostumerClient;
import com.java.fiap.ordermanager.domain.mappers.ConverterToDTO;
import com.java.fiap.ordermanager.domain.repository.OrderRepository;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock private OrderRepository orderRepository;

  @Mock private OrderTrackingService orderTracking;

  @Mock private CreateOrderUseCase createOrderUseCase;

  @Mock private ConverterToDTO converterToDTO;

  @Mock private RabbitTemplate rabbitTemplate;

  @Mock private MsCostumerClient msCostumerClient;

  @InjectMocks private OrderServiceImpl orderService;

  private Orders order;
  private UUID orderId;
  private OrderForm orderForm;

  @BeforeEach
  void setUp() {
    orderId = UUID.randomUUID();
    order = new Orders();
    order.setId(orderId);
    order.setStatus(OrderStatus.OPEN);

    orderForm =
        new OrderForm(
            "customer-id",
            true,
            OrderStatus.OPEN,
            List.of(new OrderItemForm(1L, 2)),
            new PaymentForm(10.0, "PIX", PaymentStatus.PENDING));
  }

  @Test
  void shouldReturnAllOrders() {
    when(orderRepository.findAll()).thenReturn(List.of(order));
    when(converterToDTO.convertToDTO(order)).thenReturn(new OrderDTO());

    List<OrderDTO> result = orderService.getAllOrders();

    assertEquals(1, result.size());
  }

  @Test
  void shouldReturnOrderById() {
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
    when(converterToDTO.convertToDTO(order)).thenReturn(new OrderDTO());

    OrderDTO result = orderService.getOrderById(orderId);

    assertNotNull(result);
  }

  @Test
  void shouldThrowExceptionWhenOrderNotFound() {
    when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

    assertThrows(ServicesOrderException.class, () -> orderService.getOrderById(orderId));
  }

  @Test
  void shouldCreateOrder() {
    Orders newOrder = new Orders();
    when(createOrderUseCase.execute(orderForm)).thenReturn(newOrder);
    when(orderRepository.save(newOrder)).thenReturn(newOrder);
    when(converterToDTO.convertToDTO(newOrder)).thenReturn(new OrderDTO());

    OrderDTO result = orderService.createOrder(orderForm);

    assertNotNull(result);
  }

  @Test
  void shouldDeleteOrder() {
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    orderService.deleteOrder(orderId);

    verify(orderRepository, times(1)).delete(order);
  }

  @Test
  void shouldThrowExceptionWhenDeletingDeliveredOrder() {
    order.setStatus(OrderStatus.DELIVERED);
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    assertThrows(ServicesOrderException.class, () -> orderService.deleteOrder(orderId));
  }
}
