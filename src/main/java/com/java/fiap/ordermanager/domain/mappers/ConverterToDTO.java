package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderItemDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.PaymentDTO;
import com.java.fiap.ordermanager.domain.entity.OrderItem;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.Payment;
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

  public OrderItemDTO convertToDTO(OrderItem orderItem) {
    return modelMapper.map(orderItem, OrderItemDTO.class);
  }

  public PaymentDTO convertToDTO(Payment payment) {
    return modelMapper.map(payment, PaymentDTO.class);
  }
}
