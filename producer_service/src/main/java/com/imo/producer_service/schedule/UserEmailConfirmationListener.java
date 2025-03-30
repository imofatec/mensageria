package com.imo.producer_service.schedule;

import com.imo.producer_service.config.Envs;
import com.imo.producer_service.models.outbox.Outbox;
import com.imo.producer_service.models.outbox.OutboxEvent;
import com.imo.producer_service.models.outbox.OutboxStatus;
import com.imo.producer_service.models.outbox.repositories.OutboxRepository;
import com.imo.producer_service.models.user.NoPasswordUser;
import com.imo.producer_service.schedule.interfaces.IUserEmailConfirmationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
public class UserEmailConfirmationListener implements
    IUserEmailConfirmationListener {
  private final OutboxRepository<NoPasswordUser> outboxRepository;

  private final RabbitTemplate rabbitTemplate;

  private final Envs envs;

  public UserEmailConfirmationListener(
      OutboxRepository<NoPasswordUser> outboxRepository, RabbitTemplate rabbitTemplate,
      Envs envs
  ) {
    this.outboxRepository = outboxRepository;
    this.rabbitTemplate = rabbitTemplate;
    this.envs = envs;
  }

  @Transactional
  @Scheduled(fixedDelay = 5000)
  public void execute() {
    PageRequest pageRequest = PageRequest.of(0, 10);

    List<Outbox<NoPasswordUser>> newOutboxes = outboxRepository.findByStatusAndEvent(
        OutboxStatus.PENDING,
        OutboxEvent.USER_EMAIL_CONFIRMATION,
        pageRequest
    ).getContent();


    try {
      newOutboxes.forEach(outbox -> {
        this.rabbitTemplate.convertAndSend(
            envs.getExchangeName(), envs.getRoutingKey(), outbox.getPayload());
        log.info("mensagem enviada para o message broker");
        this.outboxRepository.deleteById(outbox.getId());
        log.info("outbox apagada do repositorio");
      });
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }
}
