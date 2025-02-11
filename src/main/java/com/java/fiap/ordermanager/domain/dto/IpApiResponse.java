package com.java.fiap.ordermanager.domain.dto;

import lombok.Data;

@Data
public class IpApiResponse {

  private String status;
  private Double lat;
  private Double lon;
  private String city;
  private String country;
}
