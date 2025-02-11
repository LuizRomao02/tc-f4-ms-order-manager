package com.java.fiap.ordermanager.domain.service.usecases.create;

import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.exception.order.CreateOrderTrackingUseCaseException;
import com.java.fiap.ordermanager.domain.service.usecases.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateOrderTrackingUseCase implements UseCase<OrderTracking, OrderTracking> {

  @Override
  public OrderTracking execute(OrderTracking input) {
    if (input == null) {
      throw new CreateOrderTrackingUseCaseException("OrderTracking cannot be null");
    }

    validateCoordinates(input.getLatitude(), input.getLongitude());
    return input;
  }

  private void validateCoordinates(Double latitude, Double longitude) {
    if (latitude == null || longitude == null) {
      throw new CreateOrderTrackingUseCaseException("Latitude and Longitude cannot be null");
    }

    if (latitude < -90 || latitude > 90) {
      throw new CreateOrderTrackingUseCaseException("Latitude must be between -90 and 90");
    }

    if (longitude < -180 || longitude > 180) {
      throw new CreateOrderTrackingUseCaseException("Longitude must be between -180 and 180");
    }
  }
}
