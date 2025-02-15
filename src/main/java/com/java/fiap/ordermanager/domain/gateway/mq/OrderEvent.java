package com.java.fiap.ordermanager.domain.gateway.mq;

import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {

  private int status;
  private UUID orderNumber;
  private String address;
  private int houseNumber;
  private String postalCode;
  private LocalDate estimatedDeliveryDate;
}
