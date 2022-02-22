package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.web.multipart.MultipartFile;

public interface FilmService {

  Film addFilm(Film film);

  Film savePoster(Film film, MultipartFile posterImage);
}
