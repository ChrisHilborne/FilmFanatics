package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "roles")
public class Roles {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;
  @Column(nullable = false)
  private String name;

}
