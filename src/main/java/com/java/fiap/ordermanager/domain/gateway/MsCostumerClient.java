package com.java.fiap.ordermanager.domain.gateway;

import com.java.fiap.ordermanager.domain.dto.AddressDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tc-f4-ms-client")
public interface MsCostumerClient {

  @GetMapping("/ms_client/customers/{id}")
  ResponseEntity<Void> getCustomerById(@PathVariable String id);

  @GetMapping("/ms_client/customers/address/{id}")
  AddressDTO getAddressCustomerById(@PathVariable String id);
}
