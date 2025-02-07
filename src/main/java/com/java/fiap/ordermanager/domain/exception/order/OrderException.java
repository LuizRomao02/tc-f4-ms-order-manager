package com.java.fiap.ordermanager.domain.exception.order;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class OrderException extends RuntimeException {
  private final Instant timestamp;
  private final HttpStatus status;
  private final String message;
  private final String path;
}
