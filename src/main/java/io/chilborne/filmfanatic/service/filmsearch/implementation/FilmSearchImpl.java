package io.chilborne.filmfanatic.service.filmsearch.implementation;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.dto.FilmSearchCriteriaEnum;
import io.chilborne.filmfanatic.exception.FilmNotFoundException;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.filmsearch.FilmSearch;
import io.chilborne.filmfanatic.service.filmsearch.strategy.implementation.*;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FilmSearchImpl implements FilmSearch {

  private final FilmRepository repository;

  public FilmSearchImpl(FilmRepository repository) {
    this.repository = repository;
  }

  @Override
  public Set<Film> searchFilm(String searchParam, FilmSearchCriteriaEnum searchCriteria) {
    switch (searchCriteria) {
      case TITLE:
        return new FilmTitleSearch(repository).searchFilm(searchParam);
      case YEAR:
        return new FilmYearSearch(repository).searchFilm(searchParam);
      case MAX_DURATION:
        return new FilmMaxDurationSearch(repository).searchFilm(searchParam);
      case AVG_SCORE:
        return new FilmAverageScoreSearch(repository).searchFilm(searchParam);
      case ACTOR:
        return new FilmActorSearch(repository).searchFilm(searchParam);
      case DIRECTOR:
        return new FilmDirectorSearch(repository).searchFilm(searchParam);
      case SCREENWRITER:
        return new FilmScreenwriterSearch(repository).searchFilm(searchParam);
      case CINEMATOGRAPHER:
        return new FilmCinematographerSearch(repository).searchFilm(searchParam);
      case COMPOSER:
        return new FilmComposerSearch(repository).searchFilm(searchParam);
    }
    throw new FilmNotFoundException();
  }
}
