package com.java.fiap.ordermanager.domain.gateway;

import com.java.fiap.ordermanager.domain.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tc-f4-ms-client")
public interface MsCostumerClient {

  @GetMapping("/ms_client/customers/{id}")
  CustomerDTO getCustomerById(@PathVariable String id);
}
