package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.Payment;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.gateway.mq.GenericObjectMQ;
import com.java.fiap.ordermanager.domain.gateway.mq.OrderEvent;
import com.java.fiap.ordermanager.domain.mappers.ConverterToDTO;
import com.java.fiap.ordermanager.domain.repository.OrderRepository;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  @Value("${spring.rabbitmq.queue.ms-logistics}")
  private String queueLogisticsOrder;

  private final OrderRepository orderRepository;
  private final OrderTrackingService orderTracking;
  private final CreateOrderUseCase createOrderUseCase;
  private final ConverterToDTO converterToDTO;
  private final RabbitTemplate rabbitTemplate;

  @Override
  public List<OrderDTO> getAllOrders() {
    return orderRepository.findAll().stream().map(converterToDTO::convertToDTO).toList();
  }

  @Override
  public OrderDTO getOrderById(UUID id) {
    return converterToDTO.convertToDTO(getOneOrderById(id));
  }

  @Override
  public Boolean getOpenOrdersByCustomerId(String customerId) {
    return orderRepository.existsByCustomerIdAndStatus(customerId, OrderStatus.OPEN);
  }

  @Override
  @Transactional
  public OrderDTO createOrder(OrderForm orderForm) {
    Orders newOrder = createOrderUseCase.execute(orderForm);
    Orders orderSaved = orderRepository.save(newOrder);

    return converterToDTO.convertToDTO(orderSaved);
  }

  @Override
  @Transactional
  public OrderDTO updateOrderStatus(UUID id, OrderStatus newStatus, HttpServletRequest request) {
    Orders order = getOneOrderById(id);

    if (order.getStatus().equals(newStatus)) {
      throw new ServicesOrderException(
          "Cannot change order status to " + newStatus + ". Action not allowed.");
    }

    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
      throw new ServicesOrderException("Cannot change status of a DELIVERED or CANCELED order.");
    }

    order.setStatus(newStatus);
    orderRepository.save(order);

    OrderDTO orderDTO = converterToDTO.convertToDTO(order);

    if (newStatus == OrderStatus.SHIPPED) {
      OrderTrackingDTO trackingDTO = orderTracking.addTracking(order, request);
      orderDTO.setTracking(List.of(trackingDTO));
    }

    return orderDTO;
  }

  @Override
  @Transactional
  public void deleteOrder(UUID id) {
    Orders order = getOneOrderById(id);

    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
      throw new ServicesOrderException("Cannot change status of a DELIVERED or CANCELED order.");
    }

    orderRepository.delete(order);
  }

  @Override
  public OrderDTO payOrder(UUID id, PaymentForm paymentForm) {
    Orders order = getOneOrderById(id);
    Payment payment = order.getPayment();

    if (payment.getStatus() == PaymentStatus.PAID) {
      throw new ServicesOrderException("Cannot pay an order. Order already has a payment.");
    }

    payment.setStatus(paymentForm.status());
    payment.setPaymentMethod(paymentForm.paymentMethod());

    if (payment.getStatus() == PaymentStatus.PAID) {
      OrderEvent eventMq =
          OrderEvent.builder()
              .status(1)
              .orderNumber(order.getId())
              .address("Teste")
              .houseNumber(12)
              .postalCode("11111-111")
              .estimatedDeliveryDate(order.getEstimatedDeliveryDate())
              .build();

      // pegar info do ms customer

      sendToQueueLogistics(eventMq);
    }

    return converterToDTO.convertToDTO(orderRepository.save(order));
  }

  public Orders getOneOrderById(UUID id) {
    return orderRepository
        .findById(id)
        .orElseThrow(() -> new ServicesOrderException("Not Found Order"));
  }

  private void sendToQueueLogistics(OrderEvent orderEvent) {
    rabbitTemplate.convertAndSend(
        queueLogisticsOrder, GenericObjectMQ.builder().object(orderEvent).build());
  }
}
