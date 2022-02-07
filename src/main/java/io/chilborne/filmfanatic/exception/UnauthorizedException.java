package io.chilborne.filmfanatic.exception;

import javax.naming.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {
  public UnauthorizedException(String explanation) {
    super(explanation);
  }
}
