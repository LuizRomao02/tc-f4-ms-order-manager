package com.java.fiap.ordermanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

  private String street;
  private String number;
  private String neighborhood;
  private String city;
  private String state;
  private String zipCode;
  private String additionalInfo;
}
