package com.java.fiap.ordermanager.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.exception.tracking.ServicesOrderTrackingException;
import com.java.fiap.ordermanager.domain.mappers.ConverterToDTO;
import com.java.fiap.ordermanager.domain.mappers.OrderTrackingMapper;
import com.java.fiap.ordermanager.domain.repository.OrderTrackingRepository;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderTrackingUseCase;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTrackingServiceImplTest {

  @Mock private OrderTrackingRepository orderTrackingRepository;

  @Mock private CreateOrderTrackingUseCase createOrderTrackingUseCase;

  @Mock private ConverterToDTO converterToDTO;

  @Mock private OrderTrackingMapper orderTrackingMapper;

  @InjectMocks private OrderTrackingServiceImpl orderTrackingService;

  private Orders order;
  private UUID orderId;
  private UUID trackingId;

  @BeforeEach
  void setUp() {
    orderId = UUID.randomUUID();
    trackingId = UUID.randomUUID();
    order = new Orders();
    order.setId(orderId);
  }

  @Test
  void shouldAddTracking() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    OrderTracking orderTracking = new OrderTracking();
    when(orderTrackingMapper.toEntity(request, order)).thenReturn(orderTracking);
    when(createOrderTrackingUseCase.execute(orderTracking)).thenReturn(orderTracking);
    when(orderTrackingRepository.save(orderTracking)).thenReturn(orderTracking);
    when(converterToDTO.convertToDTO(orderTracking)).thenReturn(new OrderTrackingDTO());

    OrderTrackingDTO result = orderTrackingService.addTracking(order, request);

    assertNotNull(result);
  }

  @Test
  void shouldReturnTrackingByOrderId() {
    OrderTracking orderTracking = new OrderTracking();
    when(orderTrackingRepository.findByOrderId(orderId)).thenReturn(List.of(orderTracking));
    when(converterToDTO.convertToDTO(orderTracking)).thenReturn(new OrderTrackingDTO());

    List<OrderTrackingDTO> result = orderTrackingService.getTrackingByOrderId(orderId);

    assertEquals(1, result.size());
  }

  @Test
  void shouldDeleteTracking() {
    OrderTracking tracking = new OrderTracking();
    tracking.setId(trackingId);
    tracking.setOrder(order);
    when(orderTrackingRepository.findById(trackingId)).thenReturn(Optional.of(tracking));

    orderTrackingService.deleteTracking(orderId, trackingId);

    verify(orderTrackingRepository, times(1)).deleteById(trackingId);
  }

  @Test
  void shouldThrowExceptionWhenTrackingNotFound() {
    when(orderTrackingRepository.findById(trackingId)).thenReturn(Optional.empty());

    assertThrows(
        ServicesOrderTrackingException.class,
        () -> orderTrackingService.deleteTracking(orderId, trackingId));
  }

  @Test
  void shouldThrowExceptionWhenTrackingDoesNotBelongToOrder() {
    OrderTracking tracking = new OrderTracking();
    tracking.setId(trackingId);

    Orders anotherOrder = new Orders();
    anotherOrder.setId(UUID.randomUUID());
    tracking.setOrder(anotherOrder);

    when(orderTrackingRepository.findById(trackingId)).thenReturn(Optional.of(tracking));

    assertThrows(
        ServicesOrderTrackingException.class,
        () -> orderTrackingService.deleteTracking(orderId, trackingId));
  }
}
