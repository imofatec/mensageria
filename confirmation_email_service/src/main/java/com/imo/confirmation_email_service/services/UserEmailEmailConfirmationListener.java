package com.imo.confirmation_email_service.services;

import com.imo.confirmation_email_service.models.user.NoPasswordUser;
import com.imo.confirmation_email_service.services.interfaces.IUserEmailConfirmationListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserEmailEmailConfirmationListener implements
    IUserEmailConfirmationListener {
  private final SendUserEmailConfirmationConfirmationService sendUserEmailConfirmationService;

  public UserEmailEmailConfirmationListener(
      SendUserEmailConfirmationConfirmationService sendUserEmailConfirmationService
  ) {
    this.sendUserEmailConfirmationService = sendUserEmailConfirmationService;
  }

  @Override
  @RabbitListener(queues = "${queue_name}")
  public void execute(NoPasswordUser user) {
    this.sendUserEmailConfirmationService.execute(user);
  }
}
