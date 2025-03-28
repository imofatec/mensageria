package com.imo.confirmation_email_service.services.interfaces;

import com.imo.confirmation_email_service.models.user.NoPasswordUser;

public interface IUserEmailConfirmationListener {
  void execute(NoPasswordUser user);
}
