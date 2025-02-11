package com.java.fiap.ordermanager.domain.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderTrackingDTO {

  private UUID id;
  private UUID orderId;
  private Double latitude;
  private Double longitude;
  private LocalDateTime createdAt;
}
