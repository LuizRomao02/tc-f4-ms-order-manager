package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.Order;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.repository.OrderTrackingRepository;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderTrackingUseCase;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderTrackingServiceImpl implements OrderTrackingService {

  private final OrderTrackingRepository orderTrackingRepository;
  private final OrderService orderService;
  private final CreateOrderTrackingUseCase createOrderTrackingUseCase;

  @Transactional
  @Override
  public OrderTracking addTracking(UUID orderId, OrderTrackingForm trackingForm) {
    Order order = orderService.getOneOrderById(orderId);

    OrderTracking orderTracking =
        OrderTracking.builder()
            .order(order)
            .latitude(trackingForm.latitude())
            .longitude(trackingForm.longitude())
            .timestamp(LocalDateTime.now())
            .build();

    return orderTrackingRepository.save(createOrderTrackingUseCase.execute(orderTracking));
  }
}
