package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;

@Data
@Entity(name = "roles")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;
  @Column(name = "role",nullable = false)
  private String name;

}
