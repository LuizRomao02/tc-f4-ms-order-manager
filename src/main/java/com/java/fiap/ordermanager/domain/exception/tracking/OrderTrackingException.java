package com.java.fiap.ordermanager.domain.exception.tracking;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OrderTrackingException extends RuntimeException {
  private final Instant timestamp;
  private final HttpStatus status;
  private final String message;
  private final String path;
}
