package io.chilborne.filmfanatic.exception;

public class FilmNotFoundException extends RuntimeException {

  public FilmNotFoundException(String filmTitle) {
    super(filmTitle + " not found.");
  }
}
