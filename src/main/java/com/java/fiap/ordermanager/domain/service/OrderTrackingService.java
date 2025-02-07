package com.java.fiap.ordermanager.domain.service;

import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import java.util.UUID;

public interface OrderTrackingService {

  OrderTracking addTracking(UUID orderId, OrderTrackingForm trackingForm);
}
