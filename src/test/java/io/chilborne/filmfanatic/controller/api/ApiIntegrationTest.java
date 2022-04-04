package io.chilborne.filmfanatic.controller.api;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.domain.dto.ReviewDTO;
import io.chilborne.filmfanatic.security.jwt.JwtRequest;
import io.chilborne.filmfanatic.security.jwt.JwtResponse;
import io.chilborne.filmfanatic.service.UserService;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApiIntegrationTest {

  @LocalServerPort
  private int port;

  @Autowired
  private UserService userService;
  private static final String USERNAME = "this is a test User";
  private static final String PASSWORD = new BCryptPasswordEncoder().encode("this is a test Password");

  private String jwtToken;

  @BeforeAll
  void setup() {
      RestAssured.port = this.port;
      userService.add(new User(USERNAME, PASSWORD));
  }

  @AfterAll
  void tearDown() {
    userService.deleteUser(USERNAME);
  }

  @Test
  @Order(1)
  void login() {
    String response = given()
      .contentType("application/json")
      .body(new JwtRequest(USERNAME, PASSWORD))
    .when()
      .post("/api/auth")
    .then()
      .statusCode(200)
    .assertThat()
      .body("token", notNullValue())
    .extract().body().asString();

    response = response.split(":")[1];
    jwtToken = response.substring(1, response.length() - 2);
  }

  @Test
  @Order(2)
  void addReview() {
    ReviewDTO testDto = new ReviewDTO();
    testDto.setFilm("Kingdom of Heaven");
    testDto.setUser(USERNAME);
    testDto.setText("This is a test review.");


    given()
      .contentType("application/json")
      .header("Authorization", "Bearer " + jwtToken)
      .body(testDto)
    .when()
      .post("/api/review/new")
    .then()
      .statusCode(201)
    .body("user", equalTo(USERNAME))
    .body("film", equalTo("Kingdom of Heaven"))
    .body("date", notNullValue());

  }
}