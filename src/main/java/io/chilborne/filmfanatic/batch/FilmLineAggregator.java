package io.chilborne.filmfanatic.batch;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.batch.item.file.transform.LineAggregator;

public class FilmLineAggregator implements LineAggregator<Film> {

  private static final String CSV_DELIMITER = ",";

  @Override
  public String aggregate(Film film) {
    StringBuilder builder = new StringBuilder();
    builder.append(film.getId());
    builder.append(CSV_DELIMITER);
    builder.append(film.getTitle());
    builder.append(CSV_DELIMITER);
    builder.append(film.getYear());
    builder.append(CSV_DELIMITER);
    builder.append(film.getDuration());
    builder.append(CSV_DELIMITER);
    builder.append(film.getSynopsis());
    builder.append(CSV_DELIMITER);
    builder.append(film.getPoster());
    builder.append(CSV_DELIMITER);
    builder.append(film.getFilmDirector().getId());
    builder.append(CSV_DELIMITER);
    builder.append(film.getAvgScore());
    builder.append(CSV_DELIMITER);
    builder.append(film.getUser().getId());

    return builder.toString();
  }
}
