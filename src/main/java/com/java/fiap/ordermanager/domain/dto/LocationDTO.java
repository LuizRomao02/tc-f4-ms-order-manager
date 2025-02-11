package com.java.fiap.ordermanager.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationDTO {

  public Double latitude;
  public Double longitude;
  public String city;
  public String country;
}
