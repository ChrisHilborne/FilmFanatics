package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Review;
import org.springframework.data.repository.CrudRepository;

import java.util.Set;

public interface ReviewRepository extends CrudRepository<Review, Long> {

  Set<Review> findByFilmTitle(String title);

  Set<Review> findByUserId(long id);
}
