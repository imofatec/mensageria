package com.imo.confirmation_email_service.services;

import com.imo.confirmation_email_service.config.Envs;
import com.imo.confirmation_email_service.models.user.NoPasswordUser;
import com.imo.confirmation_email_service.services.interfaces.ISendUserEmailConfirmationService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class SendUserEmailConfirmationConfirmationService implements
    ISendUserEmailConfirmationService {

  private final Envs envs;

  private final JavaMailSender mailSender;

  private final TemplateEngine templateEngine;

  public SendUserEmailConfirmationConfirmationService(
      Envs envs, JavaMailSender mailSender, TemplateEngine templateEngine) {
    this.envs = envs;
    this.mailSender = mailSender;
    this.templateEngine = templateEngine;
  }

  @Override
  @Transactional
  public void execute(NoPasswordUser user) {
    MimeMessage message = mailSender.createMimeMessage();
    try {
      MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

      helper.setFrom(envs.getEmailUsername());
      helper.setTo(user.email());
      helper.setSubject("IMO - Confirme sua conta");

      String htmlContent = this.getTemplate(user);

      helper.setText(htmlContent, true);
      this.mailSender.send(message);

      log.info("email enviado com sucesso");
    } catch (Exception e) {
      log.error(e.getMessage());
    }
  }

  private String getTemplate(NoPasswordUser user) {
    Context context = new Context();

    context.setVariable("name", user.name());
    context.setVariable("confirmationURL", this.envs.getEmailConfirmationUrl());

    return this.templateEngine.process("confirmation_email", context);
  }
}
