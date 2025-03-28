package com.imo.confirmation_email_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class JavaMailSenderConfig {
  private final Envs envs;

  public JavaMailSenderConfig(Envs envs) {
    this.envs = envs;
  }

  @Bean
  public JavaMailSender javaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost(envs.getEmailHost());
    mailSender.setPort(envs.getEmailPort());
    mailSender.setUsername(envs.getEmailUsername());
    mailSender.setPassword(envs.getEmailPassword());

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.debug", "debug");

    return mailSender;
  }
}
