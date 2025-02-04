package com.java.fiap.ordermanager.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTracking {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Order order;

  private Double latitude;

  private Double longitude;

  private LocalDateTime timestamp;
}
