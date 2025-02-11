package com.java.fiap.ordermanager.domain.service;

import com.java.fiap.ordermanager.domain.dto.LocationDTO;

public interface GeolocationService {

  public LocationDTO getLocation(String ip);
}
