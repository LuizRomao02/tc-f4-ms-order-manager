package com.java.fiap.ordermanager.domain.entity;

import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Orders extends BaseEntity {

  private String customerId;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> items;

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderTracking> tracking;

  private LocalDate estimatedDeliveryDate;

  private Boolean expressDelivery;
}
