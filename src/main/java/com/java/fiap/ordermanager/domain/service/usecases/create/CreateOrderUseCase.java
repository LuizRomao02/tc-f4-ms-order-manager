package com.java.fiap.ordermanager.domain.service.usecases.create;

import com.java.fiap.ordermanager.domain.dto.CustomerDTO;
import com.java.fiap.ordermanager.domain.dto.form.OrderForm;
import com.java.fiap.ordermanager.domain.entity.Orders;
import com.java.fiap.ordermanager.domain.exception.order.CreateOrderUseCaseException;
import com.java.fiap.ordermanager.domain.gateway.MsCostumerClient;
import com.java.fiap.ordermanager.domain.mappers.OrderMapper;
import com.java.fiap.ordermanager.domain.service.usecases.UseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CreateOrderUseCase implements UseCase<OrderForm, Orders> {

  private final MsCostumerClient msCostumerClient;
  private final OrderMapper orderMapper;

  @Override
  public Orders execute(OrderForm input) {
    validationInput(input);

    CustomerDTO customerDTO = msCostumerClient.getCustomerById(input.customerId());

    if (customerDTO == null) {
      throw new CreateOrderUseCaseException("Customer not found");
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
    if (input.tracking() != null && input.tracking().isEmpty()) {
      throw new CreateOrderUseCaseException("Tracking cannot be empty");
    }
  }
}
