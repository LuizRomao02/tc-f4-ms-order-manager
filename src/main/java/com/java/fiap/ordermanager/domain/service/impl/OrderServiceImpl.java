package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.config.mq.OrderProducer;
import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.mappers.ConverterToOrFromDTO;
import com.java.fiap.ordermanager.domain.repository.OrderRepository;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;
  private final OrderTrackingService orderTracking;
  private final CreateOrderUseCase createOrderUseCase;
  private final ConverterToOrFromDTO converterToOrFromDTO;

  private final OrderProducer orderProducer;

  @Override
  public List<OrderDTO> getAllOrders() {
    return orderRepository.findAll().stream().map(converterToOrFromDTO::convertToDTO).toList();
  }

  @Override
  public OrderDTO getOrderById(UUID id) {
    return converterToOrFromDTO.convertToDTO(getOneOrderById(id));
  }

  @Override
  @Transactional
  public OrderDTO createOrder(OrderForm orderForm) {
    Orders newOrder = createOrderUseCase.execute(orderForm);
    Orders orderSaved = orderRepository.save(newOrder);

    /*List<OrderItemDTO> orderItemDTOS = orderSaved.getItems().stream()
        .map(converterToOrFromDTO::convertToDTO)
        .collect(Collectors.toList());

    PaymentDTO paymentDTO = converterToOrFromDTO.convertToDTO(orderSaved.getPayment());

    OrderEvent orderEvent = new OrderEvent(
        orderSaved.getId(),
        orderSaved.getCreatedAt(),
        orderItemDTOS,
        paymentDTO
    );

    orderProducer.sendOrderCreatedEvent(orderEvent);*/

    return converterToOrFromDTO.convertToDTO(orderSaved);
  }

  @Override
  @Transactional
  public OrderDTO updateOrderStatus(
      UUID id, OrderStatus newStatus, OrderTrackingForm trackingForm) {
    Orders order = getOneOrderById(id);

    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
      throw new ServicesOrderException("Cannot change status of a DELIVERED or CANCELED order.");
    }

    order.setStatus(newStatus);
    orderRepository.save(order);

    if (newStatus == OrderStatus.SHIPPED) {
      OrderTracking tracking = orderTracking.addTracking(order, trackingForm);
      order.getTracking().add(tracking);

      orderRepository.save(order);
    }

    return converterToOrFromDTO.convertToDTO(order);
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
