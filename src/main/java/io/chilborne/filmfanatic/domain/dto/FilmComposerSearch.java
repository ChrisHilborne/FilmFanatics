package io.chilborne.filmfanatic.domain.dto;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.filmsearch.FilmSearchStrategy;

import java.util.Set;

public class FilmComposerSearch implements FilmSearchStrategy {

  private final FilmRepository repository;

  public FilmComposerSearch(FilmRepository repository) {
    this.repository = repository;
  }

  @Override
  public Set<Film> searchFilm(String searchParam) {
    return repository.findByFilmComposersNameContainsOrFilmComposersSurnameContains(searchParam, searchParam);
  }
}
