package com.java.fiap.ordermanager.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTracking extends BaseEntity {

  @ManyToOne
  @JoinColumn(name = "order_id")
  private Orders order;

  private Double latitude;

  private Double longitude;
}
