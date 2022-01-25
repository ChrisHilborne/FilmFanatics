package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "people")
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column(nullable = false)
  private String name;
  @Column(nullable = false)
  private String surname;
  @Column(nullable = false)
  private PersonTypeEnum type;

  @Override
  public String toString() {
    return name + " " + surname;
  }

}
