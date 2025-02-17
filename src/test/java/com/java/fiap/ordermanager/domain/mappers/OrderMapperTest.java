package com.java.fiap.ordermanager.domain.mappers;

import static org.junit.jupiter.api.Assertions.*;

import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderItemForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.OrderItem;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.Payment;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderMapperTest {

  private OrderMapper orderMapper;
  private OrderForm orderForm;

  private static final Set<LocalDate> HOLIDAYS =
      Set.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 25));

  @BeforeEach
  void setUp() {
    orderMapper = new OrderMapper();
    orderForm =
        new OrderForm(
            1L,
            true,
            OrderStatus.OPEN,
            List.of(new OrderItemForm(1L, 2)),
            new PaymentForm(10.0, "PIX", PaymentStatus.PENDING));
  }

  @Test
  void testToEntity_ShouldConvertOrderFormToOrders() {
    Orders order = orderMapper.toEntity(orderForm);
    order.setExpressDelivery(false);

    assertNotNull(order);
    assertEquals(orderForm.customerId(), order.getCustomerId());
    assertEquals(OrderStatus.OPEN, order.getStatus());
    assertFalse(order.getExpressDelivery());

    List<OrderItem> items = order.getItems();
    assertEquals(1, items.size());
    assertEquals(1L, items.get(0).getProductId());
    assertEquals(2, items.get(0).getQuantity());

    Payment payment = order.getPayment();
    assertNotNull(payment);
    assertEquals(10.0, payment.getAmount());
    assertEquals(PaymentStatus.PENDING, payment.getStatus());
  }

  @Test
  void testCalculateEstimatedDeliveryDate_ShouldReturnCorrectDate_ForStandardDelivery() {
    OrderForm orderForm =
        new OrderForm(
            1L,
            false,
            OrderStatus.OPEN,
            List.of(new OrderItemForm(1L, 2)),
            new PaymentForm(10.0, "PIX", PaymentStatus.PENDING));

    LocalDate estimatedDate = orderMapper.toEntity(orderForm).getEstimatedDeliveryDate();

    LocalDate expectedDate = LocalDate.now();
    int businessDays = 5;
    while (businessDays > 0) {
      expectedDate = expectedDate.plusDays(1);
      if (expectedDate.getDayOfWeek() != DayOfWeek.SATURDAY
          && expectedDate.getDayOfWeek() != DayOfWeek.SUNDAY
          && !HOLIDAYS.contains(expectedDate)) {
        businessDays--;
      }
    }

    assertEquals(expectedDate, estimatedDate);
  }

  @Test
  void testCalculateEstimatedDeliveryDate_ShouldReturnCorrectDate_ForExpressDelivery() {
    OrderForm orderForm =
        new OrderForm(
            1L,
            true,
            OrderStatus.OPEN,
            List.of(new OrderItemForm(1L, 2)),
            new PaymentForm(50.0, "PIX", PaymentStatus.PENDING));

    LocalDate estimatedDate = orderMapper.toEntity(orderForm).getEstimatedDeliveryDate();

    LocalDate expectedDate = LocalDate.now();
    int businessDays = 2;
    while (businessDays > 0) {
      expectedDate = expectedDate.plusDays(1);
      if (expectedDate.getDayOfWeek() != DayOfWeek.SATURDAY
          && expectedDate.getDayOfWeek() != DayOfWeek.SUNDAY
          && !HOLIDAYS.contains(expectedDate)) {
        businessDays--;
      }
    }

    assertEquals(expectedDate, estimatedDate);
  }
}
