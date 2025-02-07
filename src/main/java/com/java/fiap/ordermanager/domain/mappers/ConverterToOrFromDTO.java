package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.Order;
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
}
