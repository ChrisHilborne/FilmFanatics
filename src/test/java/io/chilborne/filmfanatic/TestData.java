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
    ridleyScott.setName("Ridley");
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
    LOTR.setTitle("The Fellowship of the Ring");
    LOTR.addActor(orlandoBloom());
    LOTR.setDirector(peterJackson());
    LOTR.setYear(2001);
    LOTR.setDuration(178);
    LOTR.setPoster("the-fellowship-of-the-ring.jpeg");
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
    kingdomOfHeaven.setYear(2005);
    kingdomOfHeaven.setDuration(190);
    kingdomOfHeaven.setPoster("kingdom-of-heaven.jpeg");
    kingdomOfHeaven.addScore(new Score(2));
    kingdomOfHeaven.addScore(new Score(2));
    kingdomOfHeaven.addScore(new Score(3));
    return kingdomOfHeaven;
  }

}
