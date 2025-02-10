package com.java.fiap.ordermanager.domain.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderTrackingDTO {

  private UUID id;
  private OrderDTO order;
  private Double latitude;
  private Double longitude;
  private LocalDateTime timestamp;
}
