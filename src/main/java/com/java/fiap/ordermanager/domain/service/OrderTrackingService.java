package com.java.fiap.ordermanager.domain.service;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.entity.Orders;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface OrderTrackingService {

  OrderTrackingDTO addTracking(Orders order, HttpServletRequest request);

  List<OrderTrackingDTO> getTrackingByOrderId(UUID orderId);

  void deleteTracking(UUID orderId, UUID trackingId);
}
