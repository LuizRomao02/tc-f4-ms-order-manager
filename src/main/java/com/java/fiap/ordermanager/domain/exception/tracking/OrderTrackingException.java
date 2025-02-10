package com.java.fiap.ordermanager.domain.exception.tracking;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class OrderTrackingException extends RuntimeException {

  private final Instant timestamp;
  private final HttpStatus status;
  private final String message;
  private final String path;

  public OrderTrackingException(Instant timestamp, HttpStatus status, String message, String path) {
    super(message);
    this.timestamp = timestamp;
    this.status = status;
    this.message = message;
    this.path = path;
  }
}
