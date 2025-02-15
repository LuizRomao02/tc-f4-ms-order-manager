package com.java.fiap.ordermanager.domain.gateway.mq;

import com.java.fiap.ordermanager.domain.dto.OrderTrackingDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEvent {

  private List<OrderTrackingDTO> tracking;
}
