package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.domain.PersonTypeEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Long> {

  Set<Person> findByType(PersonTypeEnum type);
  Set<Person> findByNameAndSurnameAllIgnoreCase(String name, String surname);
}
