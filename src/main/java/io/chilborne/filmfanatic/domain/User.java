package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(nullable = false)
  private String username;
  @Column(nullable = false)
  private String password;
  @Column
  private String name;
  @Column
  private String surname;
  @Column
  private String email;
  @Column
  private String image;
  @Column(name = "birth_date", columnDefinition = "TIMESTAMP")
  private Date birthDate;
  @Column(name = "creation_date", columnDefinition = "TIMESTAMP")
  private LocalDate creationDate;
  @Column(name = "last_login", columnDefinition = "TIMESTAMP")
  private LocalDateTime lastLogin;
  @Column
  private boolean active;

  @Column(name = "films_reviews")
  @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
  private Set<Review> filmsReviews;
  @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
  private Set<Film> peliculas;
  @OneToMany(mappedBy = "user", cascade = CascadeType.DETACH)
  private Set<Score> scores;
  @ManyToMany(cascade = CascadeType.DETACH)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Roles> roles;

  @Override
  public String toString() {
    return username;
  }
}
