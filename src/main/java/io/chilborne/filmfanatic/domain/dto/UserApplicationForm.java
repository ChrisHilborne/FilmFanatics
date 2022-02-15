package io.chilborne.filmfanatic.domain.dto;

import io.chilborne.filmfanatic.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class UserApplicationForm extends PasswordForm {

  @NotBlank(message = "{field.mandatory}")
  private String username;
  @Size(min = 2, message = "{field.name.length}")
  private String name;
  @Size(min = 2, message = "{field.surname.length}")
  private String surname;
  @Email(message = "{field.email.valid}")
  private String email;

  private LocalDate birthDate;

  public User buildUser() {
    User user = new User();
    user.setUsername(this.username);
    user.setPassword(this.password);
    user.setName(this.name);
    user.setSurname(this.surname);
    user.setBirthDate(this.birthDate);
    user.setEmail(this.email);
    return user;
  }
}
