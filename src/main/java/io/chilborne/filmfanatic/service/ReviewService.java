package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.Review;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.domain.dto.ReviewDTO;

import java.util.Set;

public interface ReviewService {

  Review addReview(Review review);

  Set<Review> findByUsername(String username);
}
