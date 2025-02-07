package com.java.fiap.ordermanager.domain.dto;

import lombok.Data;

@Data
public class CustomerDTO {

  private String name;
  private String lastName;
  private AddressDTO address;
}
