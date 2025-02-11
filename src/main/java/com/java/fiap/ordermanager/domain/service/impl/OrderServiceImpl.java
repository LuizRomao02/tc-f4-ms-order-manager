package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.config.mq.OrderEvent;
import com.java.fiap.ordermanager.config.mq.OrderProducer;
import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderItemDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.PaymentDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.mappers.ConverterToDTO;
import com.java.fiap.ordermanager.domain.repository.OrderRepository;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderTrackingService orderTracking;
  private final CreateOrderUseCase createOrderUseCase;
  private final ConverterToDTO converterToDTO;
  private final OrderProducer orderProducer;

  @Override
  public List<OrderDTO> getAllOrders() {
    return orderRepository.findAll().stream().map(converterToDTO::convertToDTO).toList();
  }

  @Override
  public OrderDTO getOrderById(UUID id) {
    return converterToDTO.convertToDTO(getOneOrderById(id));
  }

  @Override
  @Transactional
  public OrderDTO createOrder(OrderForm orderForm) {
    Orders newOrder = createOrderUseCase.execute(orderForm);
    Orders orderSaved = orderRepository.save(newOrder);

    List<OrderItemDTO> orderItemDTOS =
        orderSaved.getItems().stream()
            .map(converterToDTO::convertToDTO)
            .collect(Collectors.toList());

    PaymentDTO paymentDTO = converterToDTO.convertToDTO(orderSaved.getPayment());

    OrderEvent orderEvent =
        new OrderEvent(orderSaved.getId(), orderSaved.getCreatedAt(), orderItemDTOS, paymentDTO);

    orderProducer.sendOrderCreatedEvent(orderEvent);

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

  public Orders getOneOrderById(UUID id) {
    return orderRepository
        .findById(id)
        .orElseThrow(() -> new ServicesOrderException("Not Found Order"));
  }
}
