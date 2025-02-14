package com.java.fiap.ordermanager.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCatalogRequest {

  private Long id;
  private Integer amount;
}
