package io.chilborne.filmfanatic.domain.dto;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ChangePasswordForm extends PasswordForm {

  private String oldPassword;

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
}
