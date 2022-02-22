package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Film;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface FilmRepository extends CrudRepository<Film, Long> {

  Optional<Film> findByTitle(String title);
  Set<Film> findByYear(int year);
  Set<Film> findByYearGreaterThan(int year);
  Set<Film> findByYearLessThan(int year);
  Set<Film> findByYearBetween(int firstYear, int lastYear);
  Set<Film> findByDurationLessThan(int duration);

  @Query("SELECT f FROM films f WHERE f.filmDirector.name = ?1 AND f.filmDirector.surname = ?2")
  Set<Film> findByDirectorNameAndDirectorSurname(String name, String surname);

  @Query("SELECT f FROM films f JOIN FETCH f.filmActors a WHERE a.name = ?1 AND a.surname = ?2")
  Set<Film> findByActorsNameAndActorsSurname(String name, String surname);

}
