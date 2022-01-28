package io.chilborne.filmfanatic.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity(name = "reviews")
@Table(indexes =
  {@Index(name = "film_title_index", columnList = "film_title"),
  @Index(name = "film_id_index", columnList = "film_id"),
  @Index(name = "user_id_index" ,columnList = "user_id")})
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column
  private String title;
  @Column
  private String textReview;
  @Column(columnDefinition = "TIMESTAMP")
  private LocalDate date;

  @ManyToOne
  @JoinColumn(name = "film_id", referencedColumnName = "id")
  @JoinColumn(name = "film_title", referencedColumnName = "title")
  private Film film;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Review review = (Review) o;

    if (title != null ? !title.equals(review.title) : review.title != null) return false;
    if (textReview != null ? !textReview.equals(review.textReview) : review.textReview != null) return false;
    if (date != null ? !date.equals(review.date) : review.date != null) return false;
    if (film != null ? !film.getTitle().equals(review.film.getTitle()) : review.film != null) return false;
    return user != null ? user.equals(review.user) : review.user == null;
  }

  @Override
  public int hashCode() {
    return this.getClass().hashCode();
  }

  @Override
  public String toString() {
    return film.getTitle() + ":" + title + ":" + date;
  }
}
