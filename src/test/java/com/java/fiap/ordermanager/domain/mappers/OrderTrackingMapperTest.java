package com.java.fiap.ordermanager.domain.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.config.IpExtractor;
import com.java.fiap.ordermanager.domain.dto.LocationDTO;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.service.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OrderTrackingMapperTest {

  @Mock private GeolocationService geoLocationService;

  @Mock private IpExtractor ipExtractor;

  @Mock private HttpServletRequest request;

  @InjectMocks private OrderTrackingMapper orderTrackingMapper;

  private Orders order;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    order = new Orders();
  }

  @Test
  void testToEntity_ShouldReturnOrderTracking_WhenValidRequestAndOrder() {
    // Arrange
    String clientIp = "192.168.1.1";
    LocationDTO locationDTO = new LocationDTO(40.7128, -74.0060, "city", "country");

    when(ipExtractor.getClientIp(request)).thenReturn(clientIp);
    when(geoLocationService.getLocation(clientIp)).thenReturn(locationDTO);

    // Act
    OrderTracking orderTracking = orderTrackingMapper.toEntity(request, order);

    // Assert
    assertNotNull(orderTracking);
    assertEquals(
        order, orderTracking.getOrder()); // Verificando se a ordem foi mapeada corretamente
    assertEquals(locationDTO.getLatitude(), orderTracking.getLatitude()); // Verificando latitude
    assertEquals(locationDTO.getLongitude(), orderTracking.getLongitude()); // Verificando longitude
  }

  @Test
  void testToEntity_ShouldReturnOrderTrackingWithNullLocation_WhenGeolocationServiceReturnsNull() {
    // Arrange
    String clientIp = "192.168.1.1";

    when(ipExtractor.getClientIp(request)).thenReturn(clientIp);
    when(geoLocationService.getLocation(clientIp))
        .thenReturn(null); // Simula retorno nulo para localização

    // Act
    OrderTracking orderTracking = orderTrackingMapper.toEntity(request, order);

    // Assert
    assertNotNull(orderTracking);
    assertEquals(
        order, orderTracking.getOrder()); // Verificando se a ordem foi mapeada corretamente
    assertNull(orderTracking.getLatitude()); // Verificando latitude nula
    assertNull(orderTracking.getLongitude()); // Verificando longitude nula
  }
}
