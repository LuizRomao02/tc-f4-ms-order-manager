package com.java.fiap.ordermanager.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationDTO {

  public Double latitude;
  public Double longitude;
  public String city;
  public String country;
}
