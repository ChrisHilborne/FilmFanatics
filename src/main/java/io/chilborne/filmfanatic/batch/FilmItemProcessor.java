package io.chilborne.filmfanatic.batch;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.batch.item.ItemProcessor;


public class FilmItemProcessor implements ItemProcessor<Film, Film> {

  @Override
  public Film process(Film film) throws Exception {
    if (!film.getMigrate()) {
      return null;
    }
    else return film;
  }
}
