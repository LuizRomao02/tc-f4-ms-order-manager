package com.java.fiap.ordermanager.domain.dto;

import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;

@Data
public class OrderDTO {

  private UUID id;
  private String customerId;
  private List<OrderItemDTO> items;
  private PaymentDTO payment;
  private OrderStatus status;
  private List<OrderTrackingDTO> tracking;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
