package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConverterToDTO {

  private final ModelMapper modelMapper;

  @Autowired
  public ConverterToDTO(ModelMapper modelMapper) {
    this.modelMapper = modelMapper;
  }

  public OrderDTO convertToDTO(Orders order) {
    return modelMapper.map(order, OrderDTO.class);
  }

  public OrderTrackingDTO convertToDTO(OrderTracking orderTracking) {
    return modelMapper.map(orderTracking, OrderTrackingDTO.class);
  }
}
