package com.imo.producer_service;

import com.imo.producer_service.config.Envs;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProducerServiceApplication implements CommandLineRunner {

  private final Envs envs;

  public ProducerServiceApplication(Envs envs) {
    this.envs = envs;
  }

  public static void main(String[] args) {
    SpringApplication.run(ProducerServiceApplication.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    System.out.println(envs.getExchangeName());
  }
}
