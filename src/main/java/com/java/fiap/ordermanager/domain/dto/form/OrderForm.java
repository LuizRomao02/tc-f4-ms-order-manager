package com.java.fiap.ordermanager.domain.dto.form;

import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import java.util.List;

public record OrderForm(
    String customerId, OrderStatus status, List<OrderItemForm> items, PaymentForm payment) {}
