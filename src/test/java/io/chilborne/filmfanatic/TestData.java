package io.chilborne.filmfanatic;

import io.chilborne.filmfanatic.domain.*;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

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
    LOTR.setFilmDirector(peterJackson());
    LOTR.setYear(2001);
    LOTR.setDuration(178);
    LOTR.setPoster("the-fellowship-of-the-ring.jpg");
    LOTR.setMigrate(false);
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
    kingdomOfHeaven.setFilmDirector(ridelyScott());
    kingdomOfHeaven.setYear(2005);
    kingdomOfHeaven.setDuration(190);
    kingdomOfHeaven.setPoster("kingdom-of-heaven.jpg");
    kingdomOfHeaven.setMigrate(true);
    kingdomOfHeaven.addScore(new Score(2));
    kingdomOfHeaven.addScore(new Score(2));
    kingdomOfHeaven.addScore(new Score(3));
    return kingdomOfHeaven;
  }

  @Bean
  public User tokioschool() {
    User tokioschool = new User();
    tokioschool.setUsername("tokioschool");
    tokioschool.addFilm(LOTR());
    LOTR().setUser(tokioschool);
    tokioschool.addFilm(kingdomOfHeaven());
    kingdomOfHeaven().setUser(tokioschool);
    LOTR().getScores().forEach(tokioschool::addScore);
    kingdomOfHeaven().getScores().forEach(tokioschool::addScore);
    return tokioschool;
  }
  @Bean
  public Review testReview() {
    Review review = new Review();
    review.setUser(tokioschool());
    review.setFilm(kingdomOfHeaven());
    review.setTitle("Test Review");
    review.setTextReview("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vulputate facilisis velit eget feugiat. Vivamus nunc felis, interdum accumsan sapien sed, feugiat scelerisque ante. Pellentesque fermentum metus ac venenatis tristique. Ut feugiat ex hendrerit, blandit turpis id, semper magna. Suspendisse velit libero, fermentum at dolor a, rhoncus interdum massa. Ut mattis eget arcu at commodo. Integer rutrum diam est, in ornare ex malesuada eleifend. Phasellus eleifend suscipit enim, ac rhoncus diam tristique a. Sed pharetra quam quis dolor sagittis posuere. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Mauris tincidunt bibendum quam, eu porta mauris tincidunt eu. Nullam a ante magna. Donec lacinia ipsum lacus, sit amet malesuada nulla facilisis ut. Nullam euismod rhoncus erat. Aliquam nec ligula ac magna volutpat ultricies.");
    tokioschool().addReview(review);
    kingdomOfHeaven().addReview(review);
    return review;
  }

}
