package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.Order;
import com.java.fiap.ordermanager.domain.entity.OrderItem;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Payment;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.mappers.ConverterToOrFromDTO;
import com.java.fiap.ordermanager.domain.repository.OrderRepository;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import java.time.LocalDateTime;
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
  private final ConverterToOrFromDTO converterToOrFromDTO;

  @Override
  public List<OrderDTO> getAllOrders() {
    return orderRepository.findAll().stream()
        .map(converterToOrFromDTO::convertToDTO)
        .collect(Collectors.toList());
  }

  @Override
  public OrderDTO getOrderById(UUID id) {
    return converterToOrFromDTO.convertToDTO(getOneOrderById(id));
  }

  @Override
  @Transactional
  public OrderDTO createOrder(OrderForm orderForm) {
    Order newOrder = createOrderUseCase.execute(converterToOrFromDTO.convertToEntity(orderForm));

    List<OrderItem> items =
        orderForm.items().stream()
            .map(
                itemForm ->
                    OrderItem.builder()
                        .order(newOrder)
                        .productId(itemForm.productId())
                        .quantity(itemForm.quantity())
                        .build())
            .toList();
    newOrder.setItems(items);

    Payment payment =
        Payment.builder()
            .order(newOrder)
            .amount(orderForm.payment().amount())
            .status(PaymentStatus.PENDING)
            .build();
    newOrder.setPayment(payment);

    List<OrderTracking> trackingList =
        orderForm.tracking().stream()
            .map(
                trackingForm ->
                    OrderTracking.builder()
                        .order(newOrder)
                        .latitude(trackingForm.latitude())
                        .longitude(trackingForm.longitude())
                        .timestamp(LocalDateTime.now())
                        .build())
            .toList();

    newOrder.setTracking(trackingList);

    return converterToOrFromDTO.convertToDTO(orderRepository.save(newOrder));
  }

  @Override
  @Transactional
  public OrderDTO updateOrderStatus(
      UUID id, OrderStatus newStatus, OrderTrackingForm trackingForm) {
    Order order = getOneOrderById(id);

    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
      throw new ServicesOrderException("Cannot change status of a DELIVERED or CANCELED order.");
    }

    order.setStatus(newStatus);
    orderRepository.save(order);

    if (newStatus == OrderStatus.SHIPPED) {
      OrderTracking tracking = orderTracking.addTracking(order.getId(), trackingForm);
      order.getTracking().add(tracking);

      orderRepository.save(order);
    }

    return converterToOrFromDTO.convertToDTO(order);
  }

  @Override
  @Transactional
  public OrderDTO deleteOrder(UUID id) {
    Order order = getOneOrderById(id);

    if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELED) {
      throw new ServicesOrderException("Cannot change status of a DELIVERED or CANCELED order.");
    }

    orderRepository.delete(order);

    return converterToOrFromDTO.convertToDTO(order);
  }

  public Order getOneOrderById(UUID id) {
    return orderRepository
        .findById(id)
        .orElseThrow(() -> new ServicesOrderException("Not Found Order"));
  }
}
