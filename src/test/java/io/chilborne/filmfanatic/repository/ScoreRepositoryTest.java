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
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestData.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class ScoreRepositoryTest {

  @Autowired
  ScoreRepository scoreRepo;

  @Qualifier("LOTR")
  private Film LOTR;
  @Autowired
  @Qualifier("kingdomOfHeaven")
  private Film kingdomOfHeaven;


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