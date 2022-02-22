package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.domain.PersonTypeEnum;

import java.util.List;
import java.util.Set;

public interface PersonService {

  Person addPerson(Person person);

  List<Person> getPeopleByType(PersonTypeEnum type);
}
