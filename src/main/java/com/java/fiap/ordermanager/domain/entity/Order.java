package com.java.fiap.ordermanager.domain.entity;

import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  private String customerId;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderItem> items;

  @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
  private Payment payment;

  @Enumerated(EnumType.STRING)
  private OrderStatus status;

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
  private List<OrderTracking> tracking;
}
