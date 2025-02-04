package com.java.fiap.ordermanager.domain.entity;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private String productId;

  private Integer quantity;
}
