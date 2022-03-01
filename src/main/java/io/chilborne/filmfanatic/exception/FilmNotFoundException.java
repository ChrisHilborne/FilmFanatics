package io.chilborne.filmfanatic.exception;

public class FilmNotFoundException extends RuntimeException {

  public FilmNotFoundException() {
    super("Film not found.");
  }
}
