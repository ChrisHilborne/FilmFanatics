package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.domain.PersonTypeEnum;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Set;

public interface PersonRepository extends CrudRepository<Person, Long> {

  List<Person> findByType(PersonTypeEnum type);
  List<Person> findByNameAndSurnameAllIgnoreCase(String name, String surname);
}
