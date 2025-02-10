package com.java.fiap.ordermanager.domain.dto;

import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import java.util.UUID;
import lombok.Data;

@Data
public class PaymentDTO {

  private UUID id;
  private Double amount;
  private PaymentStatus status;
}
