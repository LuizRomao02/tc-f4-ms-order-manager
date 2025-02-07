package com.java.fiap.ordermanager.config.openfeign;

import com.java.fiap.ordermanager.domain.dto.CustomerDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "tc-f4-ms-costumer")
public interface MsCostumerClient {

  @GetMapping("/customers/{id}")
  CustomerDTO getCustomerById(@PathVariable String id);
}
