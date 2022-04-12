package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Review;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.exception.FilmNotFoundException;
import io.chilborne.filmfanatic.exception.ReviewAlreadyExistsException;
import io.chilborne.filmfanatic.exception.UserNotFoundException;
import io.chilborne.filmfanatic.repository.FilmRepository;
import io.chilborne.filmfanatic.repository.ReviewRepository;
import io.chilborne.filmfanatic.repository.UserRepository;
import io.chilborne.filmfanatic.service.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

  private final ReviewRepository reviewRepo;
  private final UserRepository userRepository;
  private final FilmRepository filmRepository;

  public ReviewServiceImpl(ReviewRepository reviewRepo,
                           UserRepository userRepository,
                           FilmRepository filmRepository) {
    this.reviewRepo = reviewRepo;
    this.userRepository = userRepository;
    this.filmRepository = filmRepository;
  }

  @Override
  @Transactional
  public Review addReview(Review review) {
    User reviewingUser = review.getUser();
    Film reviewedFilm = review.getFilm();

    if (reviewingUser.getReviews().stream().anyMatch(r -> r.getFilm().equals(reviewedFilm))) {
      throw new ReviewAlreadyExistsException(reviewingUser.getUsername(), reviewedFilm.getTitle());
    }
    else {

      review.setUser(reviewingUser);
      review.setFilm(reviewedFilm);
      return reviewRepo.save(review);
    }
  }

  @Override
  @Transactional
  public Set<Review> findByUsername(String username) {
    return reviewRepo.findByUserUsername(username);
  }
}
