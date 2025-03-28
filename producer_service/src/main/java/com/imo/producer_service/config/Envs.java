package com.imo.producer_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Envs {
  @Value("${exchange_name}")
  private String exchangeName;

  @Value("${routing_key}")
  private String routingKey;
}
