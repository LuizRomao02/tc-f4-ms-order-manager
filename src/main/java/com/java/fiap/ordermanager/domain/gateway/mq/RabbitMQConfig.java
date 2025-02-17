package com.java.fiap.ordermanager.domain.gateway.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMQConfig {

  private static final String SUFIX_DLQ = ".dlq";

  @Value("${spring.rabbitmq.queue.ms-logistics}")
  private String queueLogisticsOrder;

  @Value("${spring.rabbitmq.host:}")
  private String host;

  @Value("${spring.rabbitmq.port:}")
  private String port;

  @Value("${spring.rabbitmq.username:}")
  private String username;

  @Value("${spring.rabbitmq.password:}")
  private String password;

  public void createQueueWithDLQ(RabbitAdmin rabbitAdmin, String queueName) {
    Queue queue =
        QueueBuilder.durable(queueName)
            .withArgument("x-dead-letter-exchange", "")
            .withArgument("x-dead-letter-routing-key", queueName + SUFIX_DLQ)
            .build();

    Queue queueDLQ = QueueBuilder.durable(queueName + SUFIX_DLQ).build();

    rabbitAdmin.declareQueue(queue);
    rabbitAdmin.declareQueue(queueDLQ);
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(host);
    connectionFactory.setPort(Integer.parseInt(port));
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);

    log.info("RabbitMQ Connection created.");

    return connectionFactory;
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

    createQueueWithDLQ(rabbitAdmin, queueLogisticsOrder);

    return rabbitAdmin;
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener(
      RabbitAdmin rabbitAdmin) {
    return event -> rabbitAdmin.initialize();
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true);
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(
      ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(messageConverter);
    return rabbitTemplate;
  }
}
