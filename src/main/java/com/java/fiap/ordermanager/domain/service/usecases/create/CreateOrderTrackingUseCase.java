package com.java.fiap.ordermanager.domain.service.usecases.create;

import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.service.usecases.UseCase;
import org.springframework.stereotype.Service;

@Service
public class CreateOrderTrackingUseCase implements UseCase<OrderTracking, OrderTracking> {

  @Override
  public OrderTracking execute(OrderTracking input) {
    if (input == null) {
      throw new IllegalArgumentException("OrderTracking cannot be null");
    }

    if (input.getLatitude() == null || input.getLongitude() == null) {
      throw new IllegalArgumentException("Latitude and Longitude cannot be null");
    }

    if (input.getLatitude() < -90 || input.getLatitude() > 90) {
      throw new IllegalArgumentException("Latitude must be between -90 and 90");
    }

    if (input.getLongitude() < -180 || input.getLongitude() > 180) {
      throw new IllegalArgumentException("Longitude must be between -180 and 180");
    }

    return input;
  }
}
