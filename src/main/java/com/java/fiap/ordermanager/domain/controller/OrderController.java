package com.java.fiap.ordermanager.domain.controller;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.PaymentForm;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Tag(name = "Order", description = "Endpoints for managing orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping
  @Operation(summary = "Get all orders", description = "Retrieve a list of all orders")
  public ResponseEntity<List<OrderDTO>> getAllOrders() {
    return ResponseEntity.ok(orderService.getAllOrders());
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Get an order by ID",
      description = "Retrieve an order by its unique identifier")
  public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID id) {
    return ResponseEntity.ok(orderService.getOrderById(id));
  }

  @GetMapping("/open_orders/{customerId}")
  @Operation(
      summary = "Get an open order by Customer ID",
      description = "Retrieve an open order by its unique identifier")
  public ResponseEntity<Boolean> getOpenOrdersByCustomerId(@PathVariable Long customerId) {
    return ResponseEntity.ok(orderService.getOpenOrdersByCustomerId(customerId));
  }

  @PostMapping
  @Operation(
      summary = "Create a new order",
      description = "Create a new order with the provided details")
  public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderForm orderForm) {
    return ResponseEntity.ok(orderService.createOrder(orderForm));
  }

  @PutMapping("/{id}/status")
  @Operation(
      summary = "Update order status",
      description = "Update the status of an order by its ID")
  public ResponseEntity<OrderDTO> updateOrderStatus(
      @PathVariable UUID id, @RequestParam OrderStatus newStatus, HttpServletRequest request) {
    return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus, request));
  }

  @PostMapping("/{id}/payment")
  @Operation(
      summary = "Make a payment for an order",
      description = "Process the payment for a specific order by its ID")
  public ResponseEntity<OrderDTO> payOrder(
      @PathVariable UUID id, @RequestBody PaymentForm paymentForm) {
    return ResponseEntity.ok(orderService.payOrder(id, paymentForm));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an order", description = "Delete an order by its ID")
  public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
    orderService.deleteOrder(id);
    return ResponseEntity.noContent().build();
  }
}
