package io.chilborne.filmfanatic.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("A user with that username or password was not found.");
  }

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(String message, Throwable cause) {
    super(message, cause);
  }
}
