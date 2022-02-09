package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
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
  private LocalDate birthDate;
  @Column(name = "creation_date", columnDefinition = "TIMESTAMP")
  private LocalDate creationDate;
  @Column(name = "last_login", columnDefinition = "TIMESTAMP")
  private LocalDateTime lastLogin;
  @Column
  private boolean active;

  @Column(name = "films_reviews")
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<Review> filmsReviews;
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<Film> films;
  @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
  private Set<Score> scores;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles;


  public void addFilm(Film film) {
    films.add(film);
  }

  public void removeFilm(Film film) {
    films.remove(film);
  }

  public void addReview(Review review) {
    filmsReviews.add(review);
  }

  public void removeReview(Review review) {
    filmsReviews.remove(review);
  }

  public void addScore(Score score) {
    scores.add(score);
  }

  public void removeScore(Score score) {
    scores.remove(score);
  }

  public void addRole(Role role) {
    roles.add(role);
  }

  public void removeRole(Role role) {
    roles.remove(role);
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    User user = (User) o;

    if (active != user.active) return false;
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

  public void update(User user) {
    if (user.getUsername() != null) {
      this.username = user.getUsername();
    }
    if (user.getEmail() != null) {
      this.email = user.getEmail();
    }
    if (user.getName() != null) {
      this.name = user.getName();
    }
    if (user.getSurname() != null) {
      this.surname = user.getSurname();
    }
  }

}
