package com.java.fiap.ordermanager.domain.service.impl;

import com.java.fiap.ordermanager.domain.dto.IpApiResponse;
import com.java.fiap.ordermanager.domain.dto.LocationDTO;
import com.java.fiap.ordermanager.domain.exception.tracking.CreateOrderTrackingUseCaseException;
import com.java.fiap.ordermanager.domain.service.GeolocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeolocationServiceImpl implements GeolocationService {

  @Value("${api-geolocation.url}")
  private String apiUrl;

  private final RestTemplate restTemplate;

  @Autowired
  public GeolocationServiceImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  @Override
  public LocationDTO getLocation(String ip) {
    String url =
        UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("ip", ip).build().toUriString();

    try {
      ResponseEntity<IpApiResponse> response = restTemplate.getForEntity(url, IpApiResponse.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        IpApiResponse data = response.getBody();

        if (data != null && "success".equals(data.getStatus())) {
          return LocationDTO.builder()
              .city(data.getCity())
              .country(data.getCountry())
              .latitude(data.getLat())
              .longitude(data.getLon())
              .build();
        }
      }
    } catch (Exception e) {
      throw new CreateOrderTrackingUseCaseException("Error obtaining geolocation data");
    }
    return null;
  }
}
