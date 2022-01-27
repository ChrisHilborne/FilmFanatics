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
@Table(indexes = @Index(name = "usrname_index", columnList = "username"))
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
  private Set<Role> roles;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (active != user.active) return false;
    if (id != null ? !id.equals(user.id) : user.id != null) return false;
    if (username != null ? !username.equals(user.username) : user.username != null) return false;
    if (password != null ? !password.equals(user.password) : user.password != null) return false;
    if (name != null ? !name.equals(user.name) : user.name != null) return false;
    if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
    if (email != null ? !email.equals(user.email) : user.email != null) return false;
    if (image != null ? !image.equals(user.image) : user.image != null) return false;
    if (birthDate != null ? !birthDate.equals(user.birthDate) : user.birthDate != null) return false;
    if (creationDate != null ? !creationDate.equals(user.creationDate) : user.creationDate != null) return false;
    return lastLogin != null ? lastLogin.equals(user.lastLogin) : user.lastLogin == null;
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return username;
  }
}
