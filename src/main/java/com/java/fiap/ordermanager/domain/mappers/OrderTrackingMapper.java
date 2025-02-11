package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.config.IpExtractor;
import com.java.fiap.ordermanager.domain.dto.LocationDTO;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.service.GeolocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderTrackingMapper {

  private final GeolocationService geoLocationService;
  private final IpExtractor ipExtractor;

  public OrderTracking toEntity(HttpServletRequest request, Orders order) {
    String ip = ipExtractor.getClientIp(request);
    LocationDTO locationData = geoLocationService.getLocation(ip);

    return OrderTracking.builder()
        .order(order)
        .latitude(locationData != null ? locationData.getLatitude() : null)
        .longitude(locationData != null ? locationData.getLongitude() : null)
        .build();
  }
}
