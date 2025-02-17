package com.java.fiap.ordermanager.domain.service.usecase.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderItemForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import com.java.fiap.ordermanager.domain.exception.order.CreateOrderUseCaseException;
import com.java.fiap.ordermanager.domain.gateway.MsCostumerClient;
import com.java.fiap.ordermanager.domain.gateway.MsProductCatalogClient;
import com.java.fiap.ordermanager.domain.mappers.OrderMapper;
import com.java.fiap.ordermanager.domain.service.usecases.create.CreateOrderUseCase;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CreateOrderUseCaseTest {

  @Mock private MsCostumerClient msCostumerClient;

  @Mock private MsProductCatalogClient msProductCatalogClient;

  @Mock private OrderMapper orderMapper;

  @InjectMocks private CreateOrderUseCase createOrderUseCase;

  private OrderForm orderForm;

  @BeforeEach
  void setUp() {
    orderForm =
        new OrderForm(
            "customer-id",
            true,
            OrderStatus.OPEN,
            List.of(new OrderItemForm(1L, 2)),
            new PaymentForm(10.0, "PIX", PaymentStatus.PENDING));
  }

  @Test
  void shouldThrowExceptionWhenCustomerNotFound() {
    when(msCostumerClient.getCustomerById(anyString()))
        .thenReturn(ResponseEntity.notFound().build());

    CreateOrderUseCaseException exception =
        assertThrows(
            CreateOrderUseCaseException.class,
            () -> {
              createOrderUseCase.execute(orderForm);
            });

    assertEquals("Customer not found", exception.getMessage());
  }

  @Test
  void shouldThrowExceptionWhenStockUpdateFails() {
    when(msCostumerClient.getCustomerById(anyString())).thenReturn(ResponseEntity.ok().build());
    when(msProductCatalogClient.updateStock(any())).thenReturn(ResponseEntity.badRequest().build());

    CreateOrderUseCaseException exception =
        assertThrows(
            CreateOrderUseCaseException.class,
            () -> {
              createOrderUseCase.execute(orderForm);
            });

    assertTrue(exception.getMessage().contains("Error updating stock"));
  }
}
