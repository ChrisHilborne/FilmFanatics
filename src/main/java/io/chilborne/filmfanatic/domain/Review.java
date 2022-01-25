package io.chilborne.filmfanatic.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "reviews")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column
  private String title;
  @Column
  private String textReview;
  @Column
  private LocalDate date;

  @ManyToOne
  @JoinColumn(name = "film_id")
  private Film film;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Override
  public String toString() {
    return film.getTitle() + ":" + title + ":" + date;
  }
}
