package com.java.fiap.ordermanager.domain.dto;

import lombok.Data;

@Data
public class AddressDTO {

  private String street;
  private String number;
  private String neighborhood;
  private String city;
  private String state;
  private String zipCode;
  private String additionalInfo;
}
