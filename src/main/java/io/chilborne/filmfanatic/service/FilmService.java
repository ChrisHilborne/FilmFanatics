package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Score;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface FilmService {

  Film addFilm(Film film);

  Film savePoster(Film film, MultipartFile posterImage);

  Film getFilmByUri(String filmUri);

  Film addScore(String filmUri, Score score);

  Set<Film> searchByTitle(String title);

  Set<Film> getAllFilms();

  Set<Film> searchFilms(String searchParam, String searchCriteria);

  Film findByTitleExact(String title);
}
