package com.java.fiap.ordermanager.domain.gateway;

import com.java.fiap.ordermanager.domain.dto.AddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tc-f4-ms-customer")
public interface MsCostumerClient {

  @GetMapping("/customers/{id}")
  ResponseEntity<Void> getCustomerById(@PathVariable Long id);

  @GetMapping("/customers/{id}/address")
  AddressDTO getAddressCustomerById(@PathVariable Long id);
}
