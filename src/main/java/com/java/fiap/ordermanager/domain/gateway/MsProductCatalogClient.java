package com.java.fiap.ordermanager.domain.gateway;

import com.java.fiap.ordermanager.domain.dto.ProductCatalogRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "tc-f4-ms-product-catalog")
public interface MsProductCatalogClient {

  @PostMapping("/api/consumer-remove-stock")
  ResponseEntity<Void> updateStock(@RequestBody ProductCatalogRequest msProductCatalog);
}
