package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.exception.UserNotFoundException;
import io.chilborne.filmfanatic.exception.UserRemovalException;
import io.chilborne.filmfanatic.exception.UsernameAlreadyExistsException;
import io.chilborne.filmfanatic.repository.UserRepository;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  public UserServiceImpl(UserRepository userRepo) {
    this.userRepo = userRepo;
  }

  @Override
  public User getUser(String username) {
    logger.info("Fetching User {}", username);
    return userRepo.findByUsernameIgnoreCase(username)
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public User getUser(long id) {
    return userRepo.findById(id)
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public Set<User> getAllUsers() {
    return userRepo.findByActive(true);
  }

  @Override
  @Transactional
  public User updateUser(String oldUsername, User user) {
    logger.info("Updating {} to " + user, oldUsername);
    // check if username is new
    if (!oldUsername.equalsIgnoreCase(user.getUsername())
    // and check if it's available
      && userRepo.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
      throw new UsernameAlreadyExistsException(user.getUsername() + " unavailable.");
    }
    User toUpdate = userRepo.findByUsernameIgnoreCase(oldUsername)
      .orElseThrow(UserNotFoundException::new);
    toUpdate.update(user);
    return userRepo.save(toUpdate);
  }

  @Override
  public void deleteUser(String username) {
    logger.info("Deleting User {}", username);
    if (!userRepo.deleteByUsername(username)) {
      throw new UserRemovalException("Error while attempting to delete User");
    }
  }
}
