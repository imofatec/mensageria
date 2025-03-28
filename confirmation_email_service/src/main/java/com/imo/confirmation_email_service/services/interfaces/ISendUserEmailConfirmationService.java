package com.imo.confirmation_email_service.services.interfaces;

import com.imo.confirmation_email_service.models.user.NoPasswordUser;

public interface ISendUserEmailConfirmationService {
  void execute(NoPasswordUser user);
}
