package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import org.springframework.stereotype.Component;

@Component
public class OrderTrackingMapper {

  public OrderTracking toEntity(OrderTrackingForm form, Orders order) {
    return OrderTracking.builder()
        .order(order)
        .latitude(form.latitude())
        .longitude(form.longitude())
        .build();
  }
}
