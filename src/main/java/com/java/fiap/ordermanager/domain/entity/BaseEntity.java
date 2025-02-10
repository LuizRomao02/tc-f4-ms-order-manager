package com.java.fiap.ordermanager.domain.entity;

import jakarta.persistence.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;

  @Column(name = "created_at", updatable = false)
  protected LocalDateTime createdAt;

  @Column(name = "updated_at")
  protected LocalDateTime updatedAt;

  @PrePersist
  public void prePersist() {
    this.createdAt = Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
    this.updatedAt = Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
  }
}
