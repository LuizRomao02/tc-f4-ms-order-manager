package com.java.fiap.ordermanager.domain.dto;

import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

  private UUID id;
  private Double amount;
  private PaymentStatus status;
}
