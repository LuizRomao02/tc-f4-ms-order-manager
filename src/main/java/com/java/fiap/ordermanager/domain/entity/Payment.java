package com.java.fiap.ordermanager.domain.entity;

import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {

  @OneToOne
  @JoinColumn(name = "order_id")
  private Orders order;

  private Double amount;

  @Enumerated(EnumType.STRING)
  private PaymentStatus status;
}
