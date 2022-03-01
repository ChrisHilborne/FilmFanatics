package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Score;
import io.chilborne.filmfanatic.exception.FilmNotFoundException;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.service.FilmService;
import io.chilborne.filmfanatic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Set;

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
  @Transactional
  public Film addFilm(Film film) {
    film.setUrl(StringUtil.getFilmUrl(film.getTitle(), film.getYear()));
    log.info("Saving Film {}", film);
    return filmRepo.save(film);
  }

  @Override
  @Transactional
  public Film savePoster(Film film, MultipartFile posterImage) {
    Film toUpdate = filmRepo.findByTitleIgnoreCase(film.getTitle())
      .orElseThrow(() -> new FilmNotFoundException(film.getTitle()));

    String fileName = StringUtil.getFilmPosterFilename(film.getTitle(), film.getYear(), posterImage.getContentType());
    fileService.saveFile(posterImage, fileName);

    toUpdate.setPoster(fileName);
    return filmRepo.save(toUpdate);
  }

  @Override
  @Transactional()
  public Film getFilmByUrl(String filmUrl) {
    String filmTitle = StringUtil.getFilmTitleFromUrl(filmUrl);
    return filmRepo.findByTitleIgnoreCase(filmTitle)
      .orElseThrow(() -> new FilmNotFoundException(filmTitle));
  }

  @Override
  @Transactional
  public Film addScore(String filmUrl, Score score) {
        Film toUpdate = getFilmByUrl(filmUrl);
        toUpdate.addScore(score);
    return filmRepo.save(toUpdate);
  }

  @Override
  public Set<Film> searchByTitle(String title) {
    log.info("Searching for Film with title like {}", title);
    return filmRepo.findByTitleContainsIgnoreCase(title);
  }

  @Override
  public Set<Film> getAllFilms() {
    return filmRepo.findAll();
  }
}
