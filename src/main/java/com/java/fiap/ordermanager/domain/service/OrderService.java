package com.java.fiap.ordermanager.domain.service;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.Order;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import java.util.List;
import java.util.UUID;

public interface OrderService {

  List<OrderDTO> getAllOrders();

  OrderDTO getOrderById(UUID id);

  Order getOneOrderById(UUID id);

  OrderDTO createOrder(OrderForm order);

  OrderDTO updateOrderStatus(UUID id, OrderStatus status, OrderTrackingForm trackingForm);

  OrderDTO deleteOrder(UUID id);
}
