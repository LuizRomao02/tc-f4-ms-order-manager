package com.java.fiap.ordermanager.domain.dto.form;

import com.java.fiap.ordermanager.domain.entity.enums.PaymentStatus;


public record PaymentForm(Double amount, String paymentMethod, PaymentStatus status) {}
