package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

  private final FilmRepository filmRepo;

  public FilmServiceImpl(FilmRepository filmRepo) {
    this.filmRepo = filmRepo;
  }

  @Override
  public Film addFilm(Film film) {
    log.info("Saving Film {}", film);
    return filmRepo.save(film);
  }
}
