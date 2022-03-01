package io.chilborne.filmfanatic.service.filmsearch;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.dto.FilmSearchCriteriaEnum;

import java.util.Set;

public interface FilmSearch {

  Set<Film> searchFilm(String searchParam, FilmSearchCriteriaEnum searchCriteria);

}
