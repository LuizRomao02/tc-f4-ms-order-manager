package com.java.fiap.ordermanager.domain.exception.order;

import java.time.Instant;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ServicesOrderException extends OrderException {

  public ServicesOrderException(String message) {
    super(Instant.now(), HttpStatus.NOT_FOUND, message, "/order");
  }
}
