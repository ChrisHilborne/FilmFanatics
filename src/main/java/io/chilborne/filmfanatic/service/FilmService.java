package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Score;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface FilmService {

  Film addFilm(Film film);

  Film savePoster(Film film, MultipartFile posterImage);

  Film getFilmByUrl(String filmUrl);

  Film addScore(String filmUrl, Score score);

  Set<Film> searchByTitle(String title);

  Set<Film> getAllFilms();
}
