package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "films")
public class Film {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(nullable = false)
  private String title;
  @Column(nullable = false)
  private int year;
  @Column
  private int duration;
  @Column
  private String synopsis;
  @Column
  private String poster;
  @Column
  private boolean migrate;
  @Column(name = "date_migrate", columnDefinition = "TIMESTAMP")
  private LocalDate dataMigrate;

  @OneToMany(mappedBy = "film")
  private Set<Score> scores;
  @OneToMany(mappedBy = "film")
  private Set<Review> reviews;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToMany
  @JoinTable(name = "film_actors", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "actor_id"))
  private Set<Person> actors;
  @ManyToMany
  @JoinTable(name = "film_musicians", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "musician_id"))
  private Set<Person> musicians;
  @ManyToMany
  @JoinTable(name = "film_screenwriters", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "screenwriter_id"))
  private Set<Person> screenwriters;
  @ManyToMany
  @JoinTable(name = "film_photographers", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "photographer_id"))
  private Set<Person> photographers;

  @Override
  public String toString() {
    return title + " : " + year;
  }
}
