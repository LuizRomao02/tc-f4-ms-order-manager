package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.Order;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConverterToOrFromDTO {

  private final ModelMapper modelMapper;

  public OrderDTO convertToDTO(Order order) {
    return modelMapper.map(order, OrderDTO.class);
  }

  public Order convertToEntity(OrderForm dto) {
    return modelMapper.map(dto, Order.class);
  }

  public OrderTrackingDTO convertToDTO(OrderTracking orderTracking) {
    return modelMapper.map(orderTracking, OrderTrackingDTO.class);
  }
}
