package io.chilborne.filmfanatic.domain;

import com.google.common.collect.Iterables;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

import static javax.persistence.CascadeType.*;

@Data
@Entity(name = "films")
@Table(indexes = {
  @Index(name = "title_index", columnList = "title"),
  @Index(name = "year_index", columnList = "year")})
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

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;
  @OneToMany(mappedBy = "film", cascade = ALL,fetch = FetchType.EAGER)
  private List<Score> scores = new ArrayList<>();
  @OneToMany(mappedBy = "film", cascade = ALL, fetch = FetchType.EAGER)
  private Set<Review> reviews = new HashSet<>();
  @ManyToOne()
  @JoinColumn(name = "director_id")
  private Person director;
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "film_actors", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "actor_id"))
  private Set<Person> actors = new HashSet<>();
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "film_musicians", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "musician_id"))
  private Set<Person> musicians = new HashSet<>();
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "film_screenwriters", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "screenwriter_id"))
  private Set<Person> screenwriters = new HashSet<>();
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(name = "film_photographers", joinColumns = @JoinColumn(name = "film_id"),
    inverseJoinColumns = @JoinColumn(name = "photographer_id"))
  private Set<Person> photographers = new HashSet<>();

  public double getAvgScore() {
    return scores.stream().map(Score::getValue).reduce(0, Integer::sum) * 1.0 / scores.size();
  }

  public void addScore(Score score) {
    score.setFilm(this);
    scores.add(score);
  }

  public boolean removeScore(Score score) {
    return scores.remove(score);
  }

  public void addReview(Review review) {
    reviews.add(review);
  }

  public boolean removeReview(Review review) {
    return reviews.remove(review);
  }

  public void addActor(Person actor) {
    if (actor.getType() == PersonTypeEnum.ACTOR) {
      actors.add(actor);
    }
  }

  public boolean removeActor(Person actor) {
    return actors.remove(actor);
  }

  public void addMusician(Person musician) {
    if (musician.getType() == PersonTypeEnum.MUSICO) {
      musicians.add(musician);
    }
  }

  public boolean removeMusician(Person musician) {
    return musicians.remove(musician);
  }

  public void addScreenwriter(Person screenwriter) {
    if (screenwriter.getType() == PersonTypeEnum.GUIONISTA) {
      screenwriters.add(screenwriter);
    }
  }

  public boolean removeScreenwriter(Person screenwriter) {
    return screenwriters.remove(screenwriter);
  }

  public void addPhotographer(Person photographer) {
    if (photographer.getType() == PersonTypeEnum.FOTOGRAPHO) {
      photographers.add(photographer);
    }
  }

  public boolean removePhotographer(Person photographer) {
    return photographers.remove(photographer);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Film film = (Film) o;

    if (year != film.year) return false;
    if (duration != film.duration) return false;
    if (migrate != film.migrate) return false;
    if (id != null ? !id.equals(film.id) : film.id != null) return false;
    if (title != null ? !title.equals(film.title) : film.title != null) return false;
    if (synopsis != null ? !synopsis.equals(film.synopsis) : film.synopsis != null) return false;
    if (poster != null ? !poster.equals(film.poster) : film.poster != null) return false;
    if (dataMigrate != null ? !dataMigrate.equals(film.dataMigrate) : film.dataMigrate != null) return false;
    if (user != null ? !user.equals(film.user) : film.user != null) return false;
    if (reviews != null ? !reviews.equals(film.reviews) : film.reviews != null) return false;
    if (director != null ? !director.equals(film.director) : film.director != null) return false;
    if (actors != null ? !actors.equals(film.actors) : film.actors != null) return false;
    if (musicians != null ? !musicians.equals(film.musicians) : film.musicians != null) return false;
    if (screenwriters != null ? !screenwriters.equals(film.screenwriters) : film.screenwriters != null) return false;
    return photographers != null ? photographers.equals(film.photographers) : film.photographers == null;
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return title + " : " + year;
  }


}
