package com.java.fiap.ordermanager.config.mq;

import com.java.fiap.ordermanager.domain.dto.OrderItemDTO;
import com.java.fiap.ordermanager.domain.dto.PaymentDTO;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderEvent {

  private UUID orderId;
  private LocalDateTime createdAt;
  private List<OrderItemDTO> items;
  private PaymentDTO payment;

  public OrderEvent(
      UUID orderId, LocalDateTime createdAtOrder, List<OrderItemDTO> items, PaymentDTO payment) {
    this.orderId = orderId;
    this.createdAt = createdAtOrder;
    this.items = items;
    this.payment = payment;
  }
}
