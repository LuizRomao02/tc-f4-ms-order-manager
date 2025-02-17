package com.java.fiap.ordermanager.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.domain.dto.AddressDTO;
import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderItemForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.Payment;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.gateway.MsCostumerClient;
import com.java.fiap.ordermanager.domain.mappers.ConverterToDTO;
import com.java.fiap.ordermanager.domain.repository.OrderRepository;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
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

  @Mock private HttpServletRequest request;

  private Orders order;
  private UUID orderId;
  private OrderForm orderForm;

  @BeforeEach
  void setUp() {
    orderId = UUID.randomUUID();
    order = new Orders();
    order.setId(orderId);
    order.setStatus(OrderStatus.OPEN);
    order.setEstimatedDeliveryDate(LocalDate.now().plusDays(1));

    Payment mockPayment = new Payment();
    mockPayment.setStatus(PaymentStatus.PENDING);
    order.setPayment(mockPayment);

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

  @Test
  void testGetOpenOrdersByCustomerId() {
    Mockito.when(
            orderRepository.existsByCustomerIdAndStatus(order.getCustomerId(), OrderStatus.OPEN))
        .thenReturn(true);

    Boolean result = orderService.getOpenOrdersByCustomerId(order.getCustomerId());
    assertTrue(result);

    Mockito.when(
            orderRepository.existsByCustomerIdAndStatus(order.getCustomerId(), OrderStatus.OPEN))
        .thenReturn(false);

    result = orderService.getOpenOrdersByCustomerId(order.getCustomerId());
    assertFalse(result);
  }

  @Test
  void testUpdateOrderStatus_SuccessfullyUpdated() {
    OrderStatus newStatus = OrderStatus.SHIPPED;

    when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));
    OrderDTO orderDTO = new OrderDTO();

    when(converterToDTO.convertToDTO(order)).thenReturn(orderDTO);
    OrderTrackingDTO trackingDTO = new OrderTrackingDTO();

    when(orderTracking.addTracking(order, request)).thenReturn(trackingDTO);
    OrderDTO result = orderService.updateOrderStatus(orderId, newStatus, request);

    assertEquals(newStatus, order.getStatus());
    assertNotNull(result);
    assertNotNull(result.getTracking());
    assertEquals(1, result.getTracking().size());
  }

  @Test
  void testUpdateOrderStatus_StatusNotChanged_ThrowsException() {
    UUID orderId = UUID.randomUUID();
    Orders order = new Orders();
    order.setId(orderId);
    order.setStatus(OrderStatus.DELIVERED); // Status inicial

    OrderStatus newStatus = OrderStatus.DELIVERED;

    when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

    ServicesOrderException exception =
        assertThrows(
            ServicesOrderException.class,
            () -> {
              orderService.updateOrderStatus(orderId, newStatus, request);
            });

    assertEquals(
        "Cannot change order status to " + newStatus + ". Action not allowed.",
        exception.getMessage());
  }

  @Test
  void testUpdateOrderStatus_OrderDelivered_ThrowsException() {
    UUID orderId = UUID.randomUUID();
    Orders order = new Orders();
    order.setId(orderId);
    order.setStatus(OrderStatus.DELIVERED);

    OrderStatus newStatus = OrderStatus.SHIPPED;

    when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

    ServicesOrderException exception =
        assertThrows(
            ServicesOrderException.class,
            () -> {
              orderService.updateOrderStatus(orderId, newStatus, request);
            });

    assertEquals("Cannot change status of a DELIVERED or CANCELED order.", exception.getMessage());
  }

  @Test
  void testUpdateOrderStatus_OrderCanceled_ThrowsException() {
    UUID orderId = UUID.randomUUID();
    Orders order = new Orders();
    order.setId(orderId);
    order.setStatus(OrderStatus.CANCELED);

    OrderStatus newStatus = OrderStatus.SHIPPED;

    when(orderRepository.findById(orderId)).thenReturn(java.util.Optional.of(order));

    ServicesOrderException exception =
        assertThrows(
            ServicesOrderException.class,
            () -> {
              orderService.updateOrderStatus(orderId, newStatus, request);
            });

    assertEquals("Cannot change status of a DELIVERED or CANCELED order.", exception.getMessage());
  }

  @Test
  void testPayOrder_ShouldThrowException_WhenPaymentAlreadyPaid() {
    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

    order.getPayment().setStatus(PaymentStatus.PAID);

    ServicesOrderException exception =
        assertThrows(
            ServicesOrderException.class,
            () -> {
              orderService.payOrder(
                  order.getId(), new PaymentForm(100.0, "credit_card", PaymentStatus.PAID));
            });

    assertEquals("Cannot pay an order. Order already has a payment.", exception.getMessage());
  }

  @Test
  void testPayOrder_ShouldChangeStatusToPaid_AndSendEventToQueue_WhenPaymentIsMade() {
    UUID orderId = UUID.randomUUID();
    PaymentForm paymentForm = new PaymentForm(100.0, "PIX", PaymentStatus.PAID);

    AddressDTO address =
        new AddressDTO(
            "Street", "123", "Neighborhood", "City", "State", "12345", "additional info");

    Orders order = new Orders();
    order.setId(orderId);
    order.setCustomerId(UUID.randomUUID().toString());
    order.setPayment(new Payment(order, 100.0, PaymentStatus.PENDING, "CREDIT_CARD"));
    order.setEstimatedDeliveryDate(LocalDate.now().plusDays(2));

    when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
    when(msCostumerClient.getAddressCustomerById(order.getCustomerId())).thenReturn(address);

    orderService.payOrder(orderId, paymentForm);

    assertEquals(PaymentStatus.PAID, order.getPayment().getStatus());

    verify(orderRepository).save(order);
  }
}
