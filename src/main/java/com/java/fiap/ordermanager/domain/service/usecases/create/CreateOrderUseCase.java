package com.java.fiap.ordermanager.domain.service.usecases.create;

import com.java.fiap.ordermanager.domain.dto.CustomerDTO;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.entity.enums.OrderStatus;
import com.java.fiap.ordermanager.domain.exception.order.CreateOrderUseCaseException;
import com.java.fiap.ordermanager.domain.gateway.MsCostumerClient;
import com.java.fiap.ordermanager.domain.service.usecases.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements UseCase<Orders, Orders> {

  private final MsCostumerClient msCostumerClient;

  @Override
  public Orders execute(Orders input) {

    if (ObjectUtils.isEmpty(input.getCustomerId())) {
      throw new CreateOrderUseCaseException("Customer cannot be empty");
    }

    if (ObjectUtils.isEmpty(input.getItems())) {
      throw new CreateOrderUseCaseException("Items cannot be empty");
    }

    if (ObjectUtils.isEmpty(input.getPayment())) {
      throw new CreateOrderUseCaseException("Payment cannot be empty");
    }

    if (ObjectUtils.isEmpty(input.getStatus())) {
      throw new CreateOrderUseCaseException("Status cannot be empty");
    }

    if (ObjectUtils.isEmpty(input.getTracking())) {
      throw new CreateOrderUseCaseException("Tracking cannot be empty");
    }

    input.setStatus(OrderStatus.OPEN);

    CustomerDTO customerDTO = msCostumerClient.getCustomerById(input.getCustomerId());

    if (customerDTO == null) {
      throw new CreateOrderUseCaseException("Customer not found");
    }

    return input;
  }
}
