package com.java.fiap.ordermanager.config.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderProducer {

  private final StreamBridge streamBridge;

  @Autowired
  public OrderProducer(StreamBridge streamBridge) {
    this.streamBridge = streamBridge;
  }

  public void sendOrderCreatedEvent(OrderEvent event) {
    streamBridge.send("orderCreatedOutput", event);
  }
}
