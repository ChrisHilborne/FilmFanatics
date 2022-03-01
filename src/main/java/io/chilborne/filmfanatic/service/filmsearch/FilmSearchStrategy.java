package io.chilborne.filmfanatic.service.filmsearch;

import io.chilborne.filmfanatic.domain.Film;

import java.util.Set;

public interface FilmSearchStrategy {

  Set<Film> searchFilm(String searchParam);

}
