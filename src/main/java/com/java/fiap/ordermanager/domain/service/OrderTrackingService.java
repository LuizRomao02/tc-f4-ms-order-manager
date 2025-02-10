package com.java.fiap.ordermanager.domain.service;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import java.util.List;
import java.util.UUID;

public interface OrderTrackingService {

  OrderTracking addTracking(Orders order, OrderTrackingForm trackingForm);

  List<OrderTrackingDTO> getTrackingByOrderId(UUID orderId);

  void deleteTracking(UUID orderId, UUID trackingId);

  OrderTrackingDTO converterDTO(OrderTracking orderTracking);
}
