package io.chilborne.filmfanatic.service.filmsearch.strategy;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.filmsearch.FilmSearchStrategy;

import java.util.Set;

public class FilmTitleSearch implements FilmSearchStrategy {

  private final FilmRepository repository;

  public FilmTitleSearch(FilmRepository repository) {
    this.repository = repository;
  }

  @Override
  public Set<Film> searchFilm(String searchParam) {
    return repository.findByTitleContainsIgnoreCase(searchParam);
  }
}
