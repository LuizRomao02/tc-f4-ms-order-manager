package com.java.fiap.ordermanager.domain.controller;

import com.java.fiap.ordermanager.domain.dto.OrderDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Endpoints for managing orders")
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

  @PostMapping
  @Operation(
      summary = "Create a new order",
      description = "Create a new order with the provided details")
  public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderForm orderForm) {
    return ResponseEntity.ok(orderService.createOrder(orderForm));
  }

  @PutMapping("/{id}/status")
  @Operation(
      summary = "Update order status",
      description = "Update the status of an order by its ID")
  public ResponseEntity<OrderDTO> updateOrderStatus(
      @PathVariable UUID id,
      @RequestParam OrderStatus newStatus,
      @RequestBody OrderTrackingForm trackingForm) {
    return ResponseEntity.ok(orderService.updateOrderStatus(id, newStatus, trackingForm));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Delete an order", description = "Delete an order by its ID")
  public ResponseEntity<OrderDTO> deleteOrder(@PathVariable UUID id) {
    return ResponseEntity.ok(orderService.deleteOrder(id));
  }
}
