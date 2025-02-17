package com.java.fiap.ordermanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IpApiResponse {

  private String status;
  private Double lat;
  private Double lon;
  private String city;
  private String country;
}
