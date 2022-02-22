package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.web.multipart.MultipartFile;

public interface FilmService {

  Film addFilm(Film film);

  void savePoster(Film film, MultipartFile posterImage);
}
