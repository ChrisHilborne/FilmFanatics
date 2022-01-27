package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.TestData;
import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.domain.PersonTypeEnum;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.ArrayList;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(TestData.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRepositoryTest {

  @Autowired
  PersonRepository repo;

  @Autowired
  @Qualifier("ridleyScott")
  Person ridleyScott;
  @Autowired
  @Qualifier("peterJackson")
  Person peterJackson;
  @Autowired
  @Qualifier("orlandoBloom")
  Person orlandoBloom;

  @BeforeAll
  public void setup() {
    repo.save(ridleyScott);
    repo.save(peterJackson);
    repo.save(orlandoBloom);
  }

  @AfterAll
  public void tearDown() {
    repo.deleteAll();
  }

  @Test
  void findByType() {
    // when
    Set<Person> returned = repo.findByType(PersonTypeEnum.ACTOR);

    // assert
    assertEquals(1, returned.size());
    Person returnedPerson = new ArrayList<>(returned).get(0);
    assertEquals(orlandoBloom, returnedPerson);
  }

  @Test
  void findByNameAndSurnameAllIgnoreCase() {
    // when
    Set<Person> returned = repo.findByNameAndSurnameAllIgnoreCase("pEtER", "JacKSon");

    // assert
    assertEquals(1, returned.size());
    Person returnedPerson = new ArrayList<>(returned).get(0);
    assertEquals(peterJackson, returnedPerson);
  }
}