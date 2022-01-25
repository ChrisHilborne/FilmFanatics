package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface FilmRepository extends CrudRepository<Film, Long> {

  Optional<Film> findByTitle(String title);
  Set<Film> findByYear(int year);
  Set<Film> findByYearGreaterThan(int year);
  Set<Film> findByYearLessThan(int year);
  Set<Film> findByYearBetween(int firstYear, int lastYear);
  Set<Film> findByDurationLessThan(int duration);

  Set<Film> findByDirectorNameAndDirectorSurname(@Param("director.name") String name, @Param("director.surname") String surname);
  Set<Film> findByActorsNameAndActorsSurname(@Param("actors.name") String name,@Param("actors.surname") String surname);

  @Query(value = "SELECT * FROM films f HAVING AVG(f.scores) >= :score", nativeQuery = true)
  Set<Film> findFilmByAverageScoreGreaterThan(int score);
}
