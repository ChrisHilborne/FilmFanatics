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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@DirtiesContext
@ContextConfiguration(classes = TestData.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class FilmRepositoryTest {

  @Autowired
  FilmRepository filmRepo;

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
    assertTrue(returnedFilms.contains(LOTR));
    assertTrue(returnedFilms.contains(kingdomOfHeaven));
  }

}