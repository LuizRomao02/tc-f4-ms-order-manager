package com.java.fiap.ordermanager.domain.service.usecase.create;

import static org.junit.jupiter.api.Assertions.*;

import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.exception.tracking.CreateOrderTrackingUseCaseException;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderTrackingUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateOrderTrackingUseCaseTest {

  @InjectMocks private CreateOrderTrackingUseCase createOrderTrackingUseCase;

  @Test
  void shouldThrowExceptionWhenInputIsNull() {
    CreateOrderTrackingUseCaseException exception =
        assertThrows(
            CreateOrderTrackingUseCaseException.class,
            () -> {
              createOrderTrackingUseCase.execute(null);
            });

    assertEquals("OrderTracking cannot be null", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForInvalidLatitude() {
    Orders order = new Orders();
    OrderTracking tracking = new OrderTracking(order, 100.0, 50.0);

    CreateOrderTrackingUseCaseException exception =
        assertThrows(
            CreateOrderTrackingUseCaseException.class,
            () -> {
              createOrderTrackingUseCase.execute(tracking);
            });

    assertEquals("Latitude must be between -90 and 90", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionForInvalidLongitude() {
    Orders order = new Orders();
    OrderTracking tracking = new OrderTracking(order, 45.0, 200.0);

    CreateOrderTrackingUseCaseException exception =
        assertThrows(
            CreateOrderTrackingUseCaseException.class,
            () -> {
              createOrderTrackingUseCase.execute(tracking);
            });

    assertEquals("Longitude must be between -180 and 180", exception.getMessage());
  }

  @Test
  void shouldReturnOrderTrackingForValidInput() {
    Orders order = new Orders();
    OrderTracking tracking = new OrderTracking(order, 45.0, 90.0);
    OrderTracking result = createOrderTrackingUseCase.execute(tracking);

    assertNotNull(result);
    assertEquals(tracking, result);
  }
}
