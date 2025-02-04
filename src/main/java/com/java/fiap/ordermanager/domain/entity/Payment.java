package com.java.fiap.ordermanager.domain.entity;

import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @OneToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private Double amount;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;
}
