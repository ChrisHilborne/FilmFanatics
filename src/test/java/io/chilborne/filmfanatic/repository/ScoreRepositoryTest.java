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
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestData.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ScoreRepositoryTest {

  @Autowired
  PersonRepository personRepository;
  @Autowired
  FilmRepository filmRepo;
  @Autowired
  ScoreRepository scoreRepo;

  @Autowired
  @Qualifier("ridleyScott")
  private Person ridleyScott;
  @Autowired
  @Qualifier("peterJackson")
  private Person peterJackson;
  @Autowired
  @Qualifier("orlandoBloom")
  private Person orlandoBloom;
  @Autowired
  @Qualifier("LOTR")
  private Film LOTR;
  @Autowired
  @Qualifier("kingdomOfHeaven")
  private Film kingdomOfHeaven;

  @BeforeAll
  public void setup() {
    personRepository.saveAll(List.of(ridleyScott, orlandoBloom, peterJackson));
    filmRepo.saveAll(List.of(LOTR, kingdomOfHeaven));
  }

  @AfterAll
  public void tearDown() {
    filmRepo.deleteAll();
    personRepository.deleteAll();
  }

  @Test
  void findAvgScoreByFilmTitle() {
    // when
    double lotrAvgScore = scoreRepo.findAvgScoreByFilmTitle(LOTR.getTitle());
    double kingdomOfHeavenAvgScore = scoreRepo.findAvgScoreByFilmTitle(kingdomOfHeaven.getTitle());

    // verify
    assertTrue(lotrAvgScore >= 4.0);
    assertTrue(kingdomOfHeavenAvgScore <= 3.0);
  }
}