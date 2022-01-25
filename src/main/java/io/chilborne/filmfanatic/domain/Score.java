package io.chilborne.filmfanatic.domain;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
@Entity(name = "score")
public class Score {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Long id;
  @Column(nullable = false)
  @Min(value = 0)
  @Max(value = 5)
  private int value;

  @ManyToOne
  @JoinColumn(name = "film_id")
  private Film film;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @Override
  public String toString() {
    return "Score:" + film + " : " + value;
  }
}
