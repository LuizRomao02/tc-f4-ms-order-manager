package com.java.fiap.ordermanager.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

  private UUID id;
  private String customerId;
  private List<OrderItemDTO> items;
  private PaymentDTO payment;
  private OrderStatus status;
  private List<OrderTrackingDTO> tracking;
  private LocalDate estimatedDeliveryDate;
  private Boolean expressDelivery;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
