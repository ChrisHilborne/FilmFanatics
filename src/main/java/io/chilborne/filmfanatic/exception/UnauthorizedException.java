package io.chilborne.filmfanatic.exception;

import org.springframework.security.authentication.BadCredentialsException;

import javax.naming.AuthenticationException;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException() {
  }

  public UnauthorizedException(String message) {
    super(message);
  }

  public UnauthorizedException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
