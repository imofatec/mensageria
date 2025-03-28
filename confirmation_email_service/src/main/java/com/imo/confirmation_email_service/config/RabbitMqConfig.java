package com.imo.confirmation_email_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
  private final Envs envs;

  public RabbitMqConfig(Envs envs) {
    this.envs = envs;
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public ApplicationListener<ApplicationReadyEvent> applicationReadyListener(
      RabbitAdmin rabbitAdmin
  ) {
    return event -> rabbitAdmin.initialize();
  }

  @Bean
  public Queue notificationQueue() {
    return new Queue(envs.getQueueName());
  }

  @Bean
  public Binding notificationBinding() {
    DirectExchange directExchange = new DirectExchange(envs.getExchangeName());
    Queue notificationQueue = new Queue(envs.getQueueName());
    return BindingBuilder
        .bind(notificationQueue)
        .to(directExchange)
        .with(envs.getRoutingKey());
  }

  @Bean
  public Jackson2JsonMessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    var rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonMessageConverter());
    return rabbitTemplate;
  }
}
