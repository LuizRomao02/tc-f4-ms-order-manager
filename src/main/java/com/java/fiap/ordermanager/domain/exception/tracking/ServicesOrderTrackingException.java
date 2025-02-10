package com.java.fiap.ordermanager.domain.exception.tracking;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServicesOrderTrackingException extends OrderTrackingException {

  public ServicesOrderTrackingException(String message) {
    super(Instant.now(), HttpStatus.NOT_FOUND, message, "/order_tracking");
  }
}
