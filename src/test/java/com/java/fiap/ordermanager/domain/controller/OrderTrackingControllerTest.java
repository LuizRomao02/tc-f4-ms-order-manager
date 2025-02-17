package com.java.fiap.ordermanager.domain.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.exception.tracking.ServicesOrderTrackingException;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class OrderTrackingControllerTest {

  private MockMvc mockMvc;
  private AutoCloseable openMocks;

  @Mock private OrderService orderService;
  @Mock private OrderTrackingService orderTrackingService;

  @BeforeEach
  void setUp() {
    this.openMocks = MockitoAnnotations.openMocks(this);
    OrderTrackingController trackingController =
        new OrderTrackingController(orderTrackingService, orderService);
    mockMvc =
        MockMvcBuilders.standaloneSetup(trackingController)
            .setControllerAdvice(new ServicesOrderTrackingException("Not Found Order"))
            .addFilter(
                (req, resp, chain) -> {
                  resp.setCharacterEncoding("UTF-8");
                  chain.doFilter(req, resp);
                },
                "/*")
            .build();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void shouldAddTrackingSuccessfully() throws Exception {
    UUID orderId = UUID.randomUUID();
    OrderTrackingDTO trackingDTO =
        new OrderTrackingDTO(UUID.randomUUID(), orderId, 10.0, 10.0, LocalDateTime.now());

    when(orderService.getOneOrderById(orderId)).thenReturn(new Orders());
    when(orderTrackingService.addTracking(any(Orders.class), any(HttpServletRequest.class)))
        .thenReturn(trackingDTO);

    mockMvc.perform(post("/order_tracking/{orderId}", orderId)).andExpect(status().isCreated());
  }

  @Test
  void shouldReturn404WhenOrderNotFound() throws Exception {
    UUID orderId = UUID.randomUUID();
    when(orderService.getOneOrderById(orderId))
        .thenThrow(new ServicesOrderException("Not Found Order"));

    mockMvc.perform(post("/tracking/{orderId}", orderId)).andExpect(status().isNotFound());
  }

  @Test
  void shouldReturnTrackingListSuccessfully() throws Exception {
    UUID orderId = UUID.randomUUID();
    UUID trackingId = UUID.randomUUID();
    LocalDateTime dateTime = LocalDateTime.now();

    List<OrderTrackingDTO> trackingList =
        List.of(new OrderTrackingDTO(trackingId, orderId, 10.0, 10.0, dateTime));

    when(orderTrackingService.getTrackingByOrderId(orderId)).thenReturn(trackingList);

    mockMvc
        .perform(get("/order_tracking/{orderId}", orderId))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(trackingId.toString()))
        .andExpect(jsonPath("$[0].orderId").value(orderId.toString()))
        .andExpect(jsonPath("$[0].latitude").value(10.0))
        .andExpect(jsonPath("$[0].longitude").value(10.0))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void shouldReturn404WhenTrackingListIsEmpty() throws Exception {
    UUID orderId = UUID.randomUUID();
    when(orderTrackingService.getTrackingByOrderId(orderId)).thenReturn(Collections.emptyList());

    mockMvc.perform(get("/order_tracking/{orderId}", orderId)).andExpect(status().isNotFound());
  }

  @Test
  void shouldDeleteTrackingSuccessfully() throws Exception {
    UUID orderId = UUID.randomUUID();
    UUID trackingId = UUID.randomUUID();

    doNothing().when(orderTrackingService).deleteTracking(orderId, trackingId);

    mockMvc
        .perform(delete("/order_tracking/{orderId}/{trackingId}", orderId, trackingId))
        .andExpect(status().isNoContent());
  }
}
