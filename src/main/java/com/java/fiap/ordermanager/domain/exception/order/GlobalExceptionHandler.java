package com.java.fiap.ordermanager.domain.exception.order;

import com.java.fiap.ordermanager.domain.exception.tracking.ServicesOrderTrackingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler({
    CreateOrderTrackingUseCaseException.class,
    CreateOrderUseCaseException.class,
    ServicesOrderException.class,
    ServicesOrderTrackingException.class
  })
  public ResponseEntity<String> handleCustomExceptions(RuntimeException ex) {
    if (ex instanceof OrderException customException) {
      return ResponseEntity.status(customException.getStatus()).body(customException.getMessage());
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body("An unexpected error occurred");
  }
}
