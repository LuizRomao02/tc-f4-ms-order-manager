package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.OrderItem;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.Payment;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

  private static final Set<LocalDate> HOLIDAYS =
      Set.of(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 25));

  public Orders toEntity(OrderForm form) {
    Orders order = Orders.builder().customerId(form.customerId()).status(form.status()).build();

    List<OrderItem> items =
        form.items().stream()
            .map(
                itemForm ->
                    OrderItem.builder()
                        .order(order)
                        .productId(itemForm.productId())
                        .quantity(itemForm.quantity())
                        .build())
            .collect(Collectors.toList());

    order.setItems(items);

    Payment payment =
        Payment.builder()
            .order(order)
            .amount(form.payment().amount())
            .status(form.payment().status())
            .build();

    order.setPayment(payment);

    order.setEstimatedDeliveryDate(calculateEstimatedDeliveryDate(form));
    order.setExpressDelivery(form.expressDelivery());

    return order;
  }

  private LocalDate calculateEstimatedDeliveryDate(OrderForm form) {
    int deliveryDays = form.expressDelivery() ? 2 : 5;

    LocalDate estimatedDate = LocalDate.now();

    while (deliveryDays > 0) {
      estimatedDate = estimatedDate.plusDays(1);

      if (isBusinessDay(estimatedDate)) {
        deliveryDays--;
      }
    }

    return estimatedDate;
  }

  private boolean isBusinessDay(LocalDate date) {
    return !(date.getDayOfWeek() == DayOfWeek.SATURDAY
        || date.getDayOfWeek() == DayOfWeek.SUNDAY
        || HOLIDAYS.contains(date));
  }
}
