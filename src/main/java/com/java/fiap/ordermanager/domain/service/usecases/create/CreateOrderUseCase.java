package com.java.fiap.ordermanager.domain.service.usecases.create;

import com.java.fiap.ordermanager.domain.dto.ProductCatalogRequest;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.dto.form.OrderItemForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.exception.order.CreateOrderUseCaseException;
import com.java.fiap.ordermanager.domain.gateway.MsCostumerClient;
import com.java.fiap.ordermanager.domain.gateway.MsProductCatalogClient;
import com.java.fiap.ordermanager.domain.mappers.OrderMapper;
import com.java.fiap.ordermanager.domain.service.usecases.UseCase;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements UseCase<OrderForm, Orders> {

  private final MsCostumerClient msCostumerClient;
  private final MsProductCatalogClient msProductCatalogClient;
  private final OrderMapper orderMapper;

  @Override
  public Orders execute(OrderForm input) {
    validationInput(input);

    try {
      ResponseEntity<Void> customerResponse = msCostumerClient.getCustomerById(input.customerId());

      if (!customerResponse.getStatusCode().is2xxSuccessful()) {
        throw new CreateOrderUseCaseException("Customer not found");
      }

      List<OrderItemForm> items = input.items();

      for (OrderItemForm item : items) {
        ResponseEntity<Void> response =
            msProductCatalogClient.updateStock(
                ProductCatalogRequest.builder()
                    .id(item.productId())
                    .amount(item.quantity())
                    .build());

        if (!response.getStatusCode().is2xxSuccessful()) {
          throw new CreateOrderUseCaseException(
              "Error updating stock. Status: " + response.getStatusCode());
        }
      }
    } catch (Exception e) {
      throw new CreateOrderUseCaseException(e.getMessage());
    }
    return orderMapper.toEntity(input);
  }

  private void validationInput(OrderForm input) {
    if (ObjectUtils.isEmpty(input.customerId())) {
      throw new CreateOrderUseCaseException("Customer cannot be empty");
    }
    if (ObjectUtils.isEmpty(input.items())) {
      throw new CreateOrderUseCaseException("Items cannot be empty");
    }
    if (ObjectUtils.isEmpty(input.payment())) {
      throw new CreateOrderUseCaseException("Payment cannot be empty");
    }
  }
}
