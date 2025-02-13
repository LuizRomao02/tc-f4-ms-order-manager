package com.java.fiap.ordermanager.domain.mappers;

import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.OrderItem;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.Payment;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderMapper {

  public Orders toEntity(OrderForm form) {
    Orders order = Orders.builder().customerId(form.customerId()).status(OrderStatus.OPEN).build();

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
            .status(PaymentStatus.PENDING)
            .build();

    order.setPayment(payment);

    return order;
  }
}
