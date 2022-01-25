package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FilmRepository extends CrudRepository<Film, Long> {

  Optional<Film> findByTitle(String name);
  Set<Film> findByYear(int year);
  Set<Film> findByDurationLessThan(int duration);

  @Query("SELECT from Film f WHERE AVG(f.scores) > ?1")
  Set<Film> findFilmByAverageScoreGreaterThan(int score);
}
