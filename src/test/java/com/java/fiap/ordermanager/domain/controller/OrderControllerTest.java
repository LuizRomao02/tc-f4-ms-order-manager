package com.java.fiap.ordermanager.domain.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.OrderItemDTO;
import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.PaymentDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import com.java.fiap.ordermanager.domain.exception.order.ServicesOrderException;
import com.java.fiap.ordermanager.domain.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class OrderControllerTest {

  private MockMvc mockMvc;
  private AutoCloseable openMocks;

  @Mock private OrderService orderService;

  private OrderDTO orderDTO;
  UUID orderId = UUID.randomUUID();

  @BeforeEach
  void setUp() {
    this.openMocks = MockitoAnnotations.openMocks(this);
    OrderController orderController = new OrderController(orderService);
    mockMvc =
        MockMvcBuilders.standaloneSetup(orderController)
            .setControllerAdvice(new ServicesOrderException("Not Found Order"))
            .addFilter(
                (req, resp, chain) -> {
                  resp.setCharacterEncoding("UTF-8");
                  chain.doFilter(req, resp);
                },
                "/*")
            .build();

    orderDTO =
        new OrderDTO(
            orderId,
            "id-teste",
            List.of(new OrderItemDTO(UUID.randomUUID(), 1L, 2)),
            new PaymentDTO(UUID.randomUUID(), 100.0, PaymentStatus.PENDING),
            OrderStatus.OPEN,
            List.of(
                new OrderTrackingDTO(UUID.randomUUID(), orderId, 0.0, 0.0, LocalDateTime.now())),
            LocalDate.now().plusDays(3),
            true,
            LocalDateTime.now(),
            LocalDateTime.now());
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  void shouldCreateOrderSuccessfully() throws Exception {
    when(orderService.createOrder(any(OrderForm.class))).thenReturn(orderDTO);

    mockMvc
        .perform(
            post("/ms_order_manager/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"customerId\":\"id-teste\",\"expressDelivery\":true,"
                        + "\"items\":[{\"productId\":1,\"quantity\":2}],"
                        + "\"status\":\"OPEN\",\"payment\":{\"amount\":100.0,\"status\":\"PENDING\"}, "
                        + "\"tracking\":{\"latitude\":0.0,\"longitude\":0.0}}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.customerId").value("id-teste"))
        .andExpect(jsonPath("$.status").value("OPEN"))
        .andExpect(jsonPath("$.items[0].productId").value(1))
        .andExpect(jsonPath("$.items[0].quantity").value(2))
        .andExpect(jsonPath("$.payment.amount").value(100.0))
        .andExpect(jsonPath("$.payment.status").value("PENDING"))
        .andExpect(jsonPath("$.expressDelivery").value(true))
        .andExpect(jsonPath("$.tracking[0].latitude").value(0.0))
        .andExpect(jsonPath("$.tracking[0].longitude").value(0.0))
        .andDo(MockMvcResultHandlers.print());

    verify(orderService, times(1)).createOrder(any(OrderForm.class));
  }

  @Test
  void shouldGetAllOrders() throws Exception {
    List<OrderDTO> orders = List.of(orderDTO);

    when(orderService.getAllOrders()).thenReturn(orders);

    mockMvc
        .perform(get("/ms_order_manager/order"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").value(orderId.toString()))
        .andExpect(jsonPath("$[0].customerId").value("id-teste"))
        .andExpect(jsonPath("$[0].status").value("OPEN"))
        .andExpect(jsonPath("$[0].items[0].productId").value(1))
        .andExpect(jsonPath("$[0].items[0].quantity").value(2))
        .andExpect(jsonPath("$[0].payment.amount").value(100.0))
        .andExpect(jsonPath("$[0].payment.status").value("PENDING"))
        .andExpect(jsonPath("$[0].expressDelivery").value(true))
        .andExpect(jsonPath("$[0].tracking[0].latitude").value(0.0))
        .andExpect(jsonPath("$[0].tracking[0].longitude").value(0.0))
        .andDo(MockMvcResultHandlers.print());

    verify(orderService, times(1)).getAllOrders();
  }

  @Test
  void shouldReturnInternalServerError_WhenOrderServiceFails() throws Exception {
    when(orderService.getAllOrders()).thenThrow(new RuntimeException("Service failure"));

    mockMvc
        .perform(get("/ms_order_manager/order"))
        .andDo(MockMvcResultHandlers.print())
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.message").value("Service failure"));

    verify(orderService, times(1)).getAllOrders();
  }

  @Test
  void shouldReturnBadRequest_WhenInvalidOrderData() throws Exception {
    mockMvc
        .perform(
            post("/ms_order_manager/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"customerId\":\"\",\"status\":\"INVALID_STATUS\",\"items\":[{\"productId\":1,\"quantity\":-2}],\"payment\":{\"amount\":100.0,\"method\":\"Credit Card\"},\"expressDelivery\":true}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid order data"));

    verify(orderService, never()).createOrder(any(OrderForm.class));
  }

  @Test
  void shouldUpdateOrderSuccessfully() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);

    UUID orderId = UUID.randomUUID();
    OrderDTO updatedOrder =
        new OrderDTO(
            orderId,
            "customerId123",
            List.of(new OrderItemDTO(UUID.randomUUID(), 1L, 2)),
            new PaymentDTO(UUID.randomUUID(), 100.0, PaymentStatus.PENDING),
            OrderStatus.SHIPPED,
            List.of(
                new OrderTrackingDTO(UUID.randomUUID(), orderId, 40.0, 90.0, LocalDateTime.now())),
            LocalDate.now().plusDays(3),
            true,
            LocalDateTime.now(),
            LocalDateTime.now());

    when(orderService.updateOrderStatus(updatedOrder.getId(), updatedOrder.getStatus(), request))
        .thenReturn(updatedOrder);

    mockMvc
        .perform(
            put("/ms_order_manager/order/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"customerId\":\"customerId123\",\"status\":\"COMPLETED\",\"items\":[{\"productId\":1,\"quantity\":2}],\"payment\":{\"amount\":100.0,\"method\":\"Credit Card\"},\"expressDelivery\":true}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.status").value("COMPLETED"))
        .andExpect(jsonPath("$.items[0].productId").value(1))
        .andExpect(jsonPath("$.items[0].quantity").value(2))
        .andDo(MockMvcResultHandlers.print());

    verify(orderService, times(1))
        .updateOrderStatus(updatedOrder.getId(), updatedOrder.getStatus(), request);
  }

  @Test
  void shouldReturnNotFound_WhenOrderNotFound() throws Exception {
    UUID id = UUID.randomUUID();
    when(orderService.getOrderById(id)).thenThrow(new ServicesOrderException("Not Found Order"));

    mockMvc
        .perform(get("/ms_order_manager/order/{id}", id))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Order not found"));

    verify(orderService, times(1)).getOrderById(id);
  }
}
