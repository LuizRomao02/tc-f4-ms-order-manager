package com.java.fiap.ordermanager.domain.dto.form;

import java.util.List;

public record OrderForm(
    String customerId,
    List<OrderItemForm> items,
    PaymentForm payment,
    List<OrderTrackingForm> tracking) {}
