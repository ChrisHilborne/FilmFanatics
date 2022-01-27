package io.chilborne.filmfanatic;

import io.chilborne.filmfanatic.domain.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Set;

@TestConfiguration
public class TestData {

  @Bean(name = "peterJackson")
  public Person peterJackson() {
    Person peterJackson = new Person();
    peterJackson.setName("Peter");
    peterJackson.setSurname("Jackson");
    peterJackson.setType(PersonTypeEnum.DIRECTOR);
    return peterJackson;
  }

  @Bean(name = "ridleyScott")
  public Person ridelyScott() {
    Person ridleyScott = new Person();
    ridleyScott.setName("Ridely");
    ridleyScott.setSurname("Scott");
    ridleyScott.setType(PersonTypeEnum.DIRECTOR);
    return ridleyScott;
  }

  @Bean(name = "orlandoBloom")
  public Person orlandoBloom() {
    Person orlandoBloom = new Person();
    orlandoBloom.setName("Orlando");
    orlandoBloom.setSurname("Bloom");
    orlandoBloom.setType(PersonTypeEnum.ACTOR);
    return orlandoBloom;
  }

  @Bean(name = "LOTR")
  public Film LOTR() {
    Film LOTR = new Film();
    LOTR.setTitle("The Lord of The Rings");
    LOTR.addActor(orlandoBloom());
    LOTR.setDirector(peterJackson());
    LOTR.addScore(new Score(5));
    LOTR.addScore(new Score(5));
    LOTR.addScore(new Score(4));
    return LOTR;
  }

  @Bean(name = "kingdomOfHeaven")
  public Film kingdomOfHeaven() {
    Film kingdomOfHeaven = new Film();
    kingdomOfHeaven.setTitle("Kingdom of Heaven");
    kingdomOfHeaven.addActor(orlandoBloom());
    kingdomOfHeaven.setDirector(ridelyScott());
    kingdomOfHeaven.addScore(new Score(2));
    kingdomOfHeaven.addScore(new Score(2));
    kingdomOfHeaven.addScore(new Score(3));
    return kingdomOfHeaven;
  }

}
