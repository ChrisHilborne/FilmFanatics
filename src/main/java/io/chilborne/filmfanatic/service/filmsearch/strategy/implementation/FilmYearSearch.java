package io.chilborne.filmfanatic.service.filmsearch.strategy.implementation;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.filmsearch.strategy.FilmSearchStrategy;

import java.util.Set;

public class FilmYearSearch implements FilmSearchStrategy {

  private final FilmRepository repository;

  public FilmYearSearch(FilmRepository repository) {
    this.repository = repository;
  }

  @Override
  public Set<Film> searchFilm(String searchParam) {
    return repository.findByYear(Integer.parseInt(searchParam));
  }
}
