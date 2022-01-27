package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.TestData;
import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Person;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@ContextConfiguration(classes = TestData.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FilmRepositoryTest {

  @Autowired
  FilmRepository filmRepo;
  @Autowired
  PersonRepository personRepo;

  @Autowired
  @Qualifier("ridleyScott")
  Person ridleyScott;
  @Autowired
  @Qualifier("peterJackson")
  Person peterJackson;
  @Autowired
  @Qualifier("orlandoBloom")
  Person orlandoBloom;
  @Autowired
  @Qualifier("LOTR")
  Film LOTR;
  @Autowired
  @Qualifier("kingdomOfHeaven")
  Film kingdomOfHeaven;

  @BeforeAll
  void setup() {
    personRepo.save(orlandoBloom);
    personRepo.save(peterJackson);
    personRepo.save(ridleyScott);

    filmRepo.save(kingdomOfHeaven);
    filmRepo.save(LOTR);
  }

  @AfterAll
  void tearDown() {
    filmRepo.deleteAll();
    personRepo.deleteAll();
  }

  @Test
  void findByDirectorNameAndDirectorSurnameShouldReturnFilmsWithNamedDirector() {
    // when
    Set<Film> returnedFilms = filmRepo.findByDirectorNameAndDirectorSurname(peterJackson.getName(), peterJackson.getSurname());

    // assert
    assertEquals(1, returnedFilms.size());

    Film returned = new ArrayList<>(returnedFilms).get(0);
    assertEquals(LOTR, returned);


  }

  @Test
  void findByActorNameAndActorSurnameShouldReturnAllFilmsWithNamedActor() {
    // when
    Set<Film> returnedFilms = filmRepo.findByActorsNameAndActorsSurname(orlandoBloom.getName(), orlandoBloom.getSurname());

    // assert
    assertEquals(2, returnedFilms.size());
  }

}