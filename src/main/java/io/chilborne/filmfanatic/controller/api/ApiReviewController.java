package io.chilborne.filmfanatic.controller.api;

import io.chilborne.filmfanatic.domain.Review;
import io.chilborne.filmfanatic.domain.dto.ReviewDTO;
import io.chilborne.filmfanatic.exception.ReviewAlreadyExistsException;
import io.chilborne.filmfanatic.service.FilmService;
import io.chilborne.filmfanatic.service.ReviewService;
import io.chilborne.filmfanatic.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/review")
@Slf4j
public class ApiReviewController {

  private final ReviewService reviewService;
  private final UserService userService;
  private final FilmService filmService;
  private final ModelMapper modelMapper;

  public ApiReviewController(ReviewService reviewService,
                             UserService userService,
                             FilmService filmService,
                             ModelMapper modelMapper) {
    this.reviewService = reviewService;
    this.userService = userService;
    this.filmService = filmService;
    this.modelMapper = modelMapper;
  }

  @PostMapping(path = "/new", consumes = "application/json", produces = {"application/json", "text/xml"})
  public ResponseEntity<?> addReview(@RequestBody ReviewDTO reviewDTO, Principal principal) {
    if (!reviewDTO.getUser().equals(principal.getName())) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
    Review received = convertToEntity(reviewDTO);
    Review added = reviewService.addReview(received);
    ReviewDTO addedDTO = convertToDto(added);
    return ResponseEntity.status(HttpStatus.CREATED).body(addedDTO);
  }

  @GetMapping(path = "/user/{username}")
  public ResponseEntity<?> getUserReviews(@PathVariable String username, Authentication auth) {
    if (!username.equals(auth.getName())) {
      if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
      }
    }
    Set<Review> userReviews = reviewService.findByUsername(username);
    return ResponseEntity.ok(convertToDtos(userReviews));
  }

  @ExceptionHandler({ReviewAlreadyExistsException.class})
  public ResponseEntity<?> handleReviewAlreadyExistsException(HttpServletRequest request, Exception e) {
    ErrorResponse response = new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT, e.getMessage(), "/api/review/new");
    return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
  }

  private Review convertToEntity(ReviewDTO reviewDTO) {
    Review review = modelMapper.map(reviewDTO, Review.class);
    review.setTextReview(reviewDTO.getText());
    review.setUser(userService.getUser(reviewDTO.getUser()));
    review.setFilm(filmService.findByTitleExact(reviewDTO.getFilm()));
    return review;
  }

  private ReviewDTO convertToDto(Review review) {
    ReviewDTO dto = modelMapper.map(review, ReviewDTO.class);
    dto.setFilm(review.getFilm().getTitle());
    return dto;
  }

  private Set<ReviewDTO> convertToDtos(Set<Review> reviews) {
    return reviews.stream().map(this::convertToDto).collect(Collectors.toSet());
  }





}
