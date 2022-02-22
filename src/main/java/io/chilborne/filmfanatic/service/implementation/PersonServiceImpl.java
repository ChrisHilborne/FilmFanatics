package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.domain.PersonTypeEnum;
import io.chilborne.filmfanatic.repository.PersonRepository;
import io.chilborne.filmfanatic.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class PersonServiceImpl implements PersonService {

  private final PersonRepository personRepo;

  public PersonServiceImpl(PersonRepository personRepo) {
    this.personRepo = personRepo;
  }

  @Override
  public Person addPerson(Person person) {
    log.info("Adding Person: {}", person);
    return personRepo.save(person);
  }

  @Override
  public List<Person> getPeopleByType(PersonTypeEnum type) {
    log.info("Fetching Person of PersonTypeEnum {}", type);
    return personRepo.findByType(type);
  }
}
