package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
public class FilmServiceImpl implements FilmService {

  private final FilmRepository filmRepo;
  private final FileService fileService;

  public FilmServiceImpl(FilmRepository filmRepo,
                         @Qualifier("film-poster-file-service") FileService fileService) {
    this.filmRepo = filmRepo;
    this.fileService = fileService;
  }

  @Override
  public Film addFilm(Film film) {
    log.info("Saving Film {}", film);
    return filmRepo.save(film);
  }

  @Override
  public void savePoster(Film film, MultipartFile posterImage) {

  }
}
