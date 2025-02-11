package com.java.fiap.ordermanager.domain.exception.tracking;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CreateOrderTrackingUseCaseException extends OrderTrackingException {

  public CreateOrderTrackingUseCaseException(String message) {
    super(Instant.now(), HttpStatus.BAD_REQUEST, message, "/order_tracking");
  }
}
