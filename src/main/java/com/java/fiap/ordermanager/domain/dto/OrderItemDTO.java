package com.java.fiap.ordermanager.domain.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDTO {

  private UUID id;
  private Long productId;
  private Integer quantity;
}
