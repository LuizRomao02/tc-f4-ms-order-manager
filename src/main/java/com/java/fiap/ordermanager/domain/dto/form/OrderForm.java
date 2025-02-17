package com.java.fiap.ordermanager.domain.dto.form;

import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record OrderForm(
    @NotNull(message = "Customer ID is required") Long customerId,
    @NotNull(message = "Express Delivery is required") Boolean expressDelivery,
    @NotNull(message = "Status is required") OrderStatus status,
    @NotEmpty(message = "Items cannot be empty") List<OrderItemForm> items,
    @NotNull(message = "Payment information is required") PaymentForm payment) {}
