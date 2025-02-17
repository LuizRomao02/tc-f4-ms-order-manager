package com.java.fiap.ordermanager.domain.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.entity.OrderTracking;
import com.java.fiap.ordermanager.domain.entity.Orders;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ConverterToDTOTest {

  private ConverterToDTO converterToDTO;
  private ModelMapper modelMapper;

  @BeforeEach
  void setUp() {
    modelMapper = mock(ModelMapper.class);
    converterToDTO = new ConverterToDTO(modelMapper);
  }

  @Test
  void testConvertToDTO_ShouldConvertOrdersToOrderDTO() {
    Orders order = new Orders();
    OrderDTO expectedDTO = new OrderDTO();

    when(modelMapper.map(order, OrderDTO.class)).thenReturn(expectedDTO);

    OrderDTO result = converterToDTO.convertToDTO(order);

    assertEquals(expectedDTO, result);
    verify(modelMapper).map(order, OrderDTO.class);
  }

  @Test
  void testConvertToDTO_ShouldConvertOrderTrackingToOrderTrackingDTO() {
    OrderTracking orderTracking = new OrderTracking();
    OrderTrackingDTO expectedDTO = new OrderTrackingDTO();

    when(modelMapper.map(orderTracking, OrderTrackingDTO.class)).thenReturn(expectedDTO);

    OrderTrackingDTO result = converterToDTO.convertToDTO(orderTracking);

    assertEquals(expectedDTO, result);
    verify(modelMapper).map(orderTracking, OrderTrackingDTO.class);
  }
}
