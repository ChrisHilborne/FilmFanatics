package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.exception.UnauthorizedException;
import io.chilborne.filmfanatic.exception.UserNotFoundException;
import io.chilborne.filmfanatic.exception.UsernameAlreadyExistsException;
import io.chilborne.filmfanatic.repository.UserRepository;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.service.UserService;
import io.chilborne.filmfanatic.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;
  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
  private final FileService fileService;

  public UserServiceImpl(UserRepository userRepo, @Qualifier("user-image-file-service") FileService fileService) {
    this.userRepo = userRepo;
    this.fileService = fileService;
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
  @Transactional
  public void deleteUser(String username) {
    logger.info("Deleting User {}", username);
   userRepo.deleteByUsername(username);
  }

  @Override
  @Transactional
  public void changePassword(String username, String oldPassword, String newPassword) {
    logger.info("Changing User's {} password", username);
    User toUpdate = userRepo.findByUsernameIgnoreCase(username).orElseThrow(UserNotFoundException::new);
    // check user has entered correct old password
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(-1);
    if (!encoder.matches(oldPassword, toUpdate.getPassword())) {
      throw new UnauthorizedException("Authorization Failure");
    }
    else {
      toUpdate.setPassword(encoder.encode(newPassword));
      userRepo.save(toUpdate);
    }
  }

  @Override
  @Transactional
  public void saveUserImage(String username, MultipartFile imageFile) {
    String imageFileName = StringUtil.getUserImageFileName(username, imageFile.getContentType());
    User toUpdate = userRepo.findByUsernameIgnoreCase(username).orElseThrow(UserNotFoundException::new);

    // check to see if imageFileName is different from that saved with user
    if (!toUpdate.getImage().equals(imageFileName)) {
      // delete existing file, save new file and update User
      fileService.saveUserImage(imageFile, imageFileName, toUpdate.getImage());
      toUpdate.setImage(imageFileName);
      userRepo.save(toUpdate);
    }
    else {
      fileService.saveUserImage(imageFile, imageFileName);
    }
  }
}
