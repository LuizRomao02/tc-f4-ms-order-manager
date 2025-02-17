package com.java.fiap.ordermanager.domain.service;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

public interface OrderService {

  List<OrderDTO> getAllOrders();

  OrderDTO getOrderById(UUID id);

  Boolean getOpenOrdersByCustomerId(Long customerId);

  Orders getOneOrderById(UUID id);

  OrderDTO createOrder(OrderForm order);

  OrderDTO updateOrderStatus(UUID id, OrderStatus status, HttpServletRequest request);

  void deleteOrder(UUID id);

  OrderDTO payOrder(UUID id, PaymentForm paymentForm);
}
