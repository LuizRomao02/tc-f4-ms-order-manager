package com.java.fiap.ordermanager.domain.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class OrderItemDTO {

  private UUID id;
  private String productId;
  private Integer quantity;
}
