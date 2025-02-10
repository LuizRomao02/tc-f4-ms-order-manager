package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.Order;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.exception.tracking.ServicesOrderTrackingException;
import com.java.fiap.ordermanager.domain.mappers.ConverterToOrFromDTO;
import com.java.fiap.ordermanager.domain.repository.OrderTrackingRepository;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderTrackingUseCase;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderTrackingServiceImpl implements OrderTrackingService {

  private final OrderTrackingRepository orderTrackingRepository;
  private final OrderService orderService;
  private final CreateOrderTrackingUseCase createOrderTrackingUseCase;
  private final ConverterToOrFromDTO converterToOrFromDTO;

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

  @Override
  public List<OrderTrackingDTO> getTrackingByOrderId(UUID orderId) {
    return orderTrackingRepository.findByOrderId(orderId).stream()
        .map(converterToOrFromDTO::convertToDTO)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public void deleteTracking(UUID orderId, UUID trackingId) {
    OrderTracking tracking =
        orderTrackingRepository
            .findById(trackingId)
            .orElseThrow(() -> new ServicesOrderTrackingException("Tracking record not found."));

    if (!tracking.getOrder().getId().equals(orderId)) {
      throw new ServicesOrderTrackingException(
          "Tracking record does not belong to the specified order.");
    }

    orderTrackingRepository.deleteById(trackingId);
  }

  @Override
  public OrderTrackingDTO converterDTO(OrderTracking orderTracking) {
    return converterToOrFromDTO.convertToDTO(orderTracking);
  }
}
