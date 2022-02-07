package io.chilborne.filmfanatic.domain;

import lombok.Data;

@Data
public class ChangePassword {

  private String oldPassword;
  private String newPassword;
}
