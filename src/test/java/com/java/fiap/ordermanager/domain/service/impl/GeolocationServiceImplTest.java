package com.java.fiap.ordermanager.domain.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.domain.dto.IpApiResponse;
import com.java.fiap.ordermanager.domain.dto.LocationDTO;
import com.java.fiap.ordermanager.domain.exception.tracking.CreateOrderTrackingUseCaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@ExtendWith(MockitoExtension.class)
class GeolocationServiceImplTest {

  @Mock private RestTemplate restTemplate;

  @InjectMocks private GeolocationServiceImpl geolocationService;

  @BeforeEach
  void setUp() {
    geolocationService = new GeolocationServiceImpl(restTemplate);
    ReflectionTestUtils.setField(geolocationService, "apiUrl", "http://api.example.com");
  }

  @Test
  void shouldReturnLocationDTOWhenApiCallIsSuccessful() {
    String ip = "192.168.0.1";
    String apiUrl = "http://api.example.com";
    String url = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("ip", ip).toUriString();

    IpApiResponse response = new IpApiResponse("success", 10.0, 20.0, "City", "Country");

    when(restTemplate.getForEntity(eq(url), eq(IpApiResponse.class)))
        .thenReturn(ResponseEntity.ok(response));

    LocationDTO location = geolocationService.getLocation(ip);

    assertNotNull(location);
    assertEquals("City", location.getCity());
    assertEquals("Country", location.getCountry());
    assertEquals(10.0, location.getLatitude());
    assertEquals(20.0, location.getLongitude());
  }

  @Test
  void shouldThrowExceptionWhenApiCallFails() {
    String ip = "192.168.0.1";
    String apiUrl = "http://api.example.com";
    String url = UriComponentsBuilder.fromHttpUrl(apiUrl).queryParam("ip", ip).toUriString();

    when(restTemplate.getForEntity(eq(url), eq(IpApiResponse.class)))
        .thenThrow(new RuntimeException("API error"));

    CreateOrderTrackingUseCaseException exception =
        assertThrows(
            CreateOrderTrackingUseCaseException.class,
            () -> {
              geolocationService.getLocation(ip);
            });

    assertEquals("Error obtaining geolocation data", exception.getMessage());
  }
}
