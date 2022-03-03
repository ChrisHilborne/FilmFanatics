package io.chilborne.filmfanatic.controller.api;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

  private String timestamp;
  private String status;
  private String error;
  private String message;
  private String path;

  public ErrorResponse(LocalDateTime time, HttpStatus status, String message, String path) {
    this.timestamp = time.toString();
    this.status = String.valueOf(status.value());
    this.error = status.getReasonPhrase();
    this.message = message;
    this.path = path;
  }

}
