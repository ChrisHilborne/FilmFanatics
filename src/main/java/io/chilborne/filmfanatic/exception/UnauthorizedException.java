package io.chilborne.filmfanatic.exception;

import javax.naming.AuthenticationException;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
  }

  public UnauthorizedException(String message) {
    super(message);
  }
}
