package com.java.fiap.ordermanager.domain.controller;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderTrackingForm;
import com.java.fiap.ordermanager.domain.service.OrderService;
import com.java.fiap.ordermanager.domain.service.OrderTrackingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order_tracking")
@Tag(name = "Order Tracking", description = "APIs for managing order tracking")
public class OrderTrackingController {

  private final OrderTrackingService orderTrackingService;
  private final OrderService orderService;

  @Autowired
  public OrderTrackingController(
      OrderTrackingService orderTrackingService, OrderService orderService) {
    this.orderTrackingService = orderTrackingService;
    this.orderService = orderService;
  }

  @PostMapping(value = "/{orderId}")
  @Operation(
      summary = "Add tracking to an order",
      description = "Adds a new tracking record to an existing order.")
  @ApiResponse(responseCode = "201", description = "Tracking entry added successfully")
  @ApiResponse(responseCode = "400", description = "Invalid request data")
  @ApiResponse(responseCode = "404", description = "Order not found")
  @ApiResponse(responseCode = "409", description = "Cannot track a DELIVERED or CANCELED order")
  public ResponseEntity<OrderTrackingDTO> addTracking(
      @PathVariable UUID orderId, @Valid @RequestBody OrderTrackingForm trackingForm) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            orderTrackingService.converterDTO(
                orderTrackingService.addTracking(
                    orderService.getOneOrderById(orderId), trackingForm)));
  }

  @GetMapping(value = "/{orderId}")
  @Operation(
      summary = "Get tracking history for an order",
      description = "Fetches all tracking records associated with an order.")
  @ApiResponse(responseCode = "200", description = "Tracking records retrieved successfully")
  @ApiResponse(responseCode = "404", description = "Order not found")
  public ResponseEntity<List<OrderTrackingDTO>> getTrackingByOrderId(@PathVariable UUID orderId) {
    List<OrderTrackingDTO> trackingList = orderTrackingService.getTrackingByOrderId(orderId);
    return ResponseEntity.ok(trackingList);
  }

  @DeleteMapping("/{orderId}/{trackingId}")
  @Operation(
      summary = "Delete a tracking entry",
      description = "Removes a specific tracking record by its ID.")
  @ApiResponse(responseCode = "204", description = "Tracking entry deleted successfully")
  @ApiResponse(responseCode = "404", description = "Tracking entry not found")
  public ResponseEntity<Void> deleteTracking(
      @PathVariable UUID orderId, @PathVariable UUID trackingId) {
    orderTrackingService.deleteTracking(orderId, trackingId);
    return ResponseEntity.noContent().build();
  }
}
