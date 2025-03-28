package com.imo.confirmation_email_service.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Envs {
  @Value("${queue_name}")
  private String queueName;

  @Value("${exchange_name}")
  private String exchangeName;

  @Value("${routing_key}")
  private String routingKey;

  @Value("${spring.mail.host}")
  private String emailHost;

  @Value("${spring.mail.port}")
  private int emailPort;

  @Value("${spring.mail.username}")
  private String emailUsername;

  @Value("${spring.mail.password}")
  private String emailPassword;

  @Value("${email.confirmation.url}")
  private String emailConfirmationUrl;
}
