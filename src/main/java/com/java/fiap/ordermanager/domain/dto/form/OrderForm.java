package com.java.fiap.ordermanager.domain.dto.form;

import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import java.util.List;

public record OrderForm(
    String customerId,
    Boolean expressDelivery,
    OrderStatus status,
    List<OrderItemForm> items,
    PaymentForm payment) {}
