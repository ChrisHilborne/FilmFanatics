package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.exception.UnauthorizedException;
import io.chilborne.filmfanatic.exception.UserNotFoundException;
import io.chilborne.filmfanatic.exception.UsernameAlreadyExistsException;
import io.chilborne.filmfanatic.repository.RoleRepository;
import io.chilborne.filmfanatic.repository.UserRepository;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.service.UserService;
import io.chilborne.filmfanatic.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Set;

import static io.chilborne.filmfanatic.util.Constants.DEFAULT_PROFILE_IMAGE;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepo;
  private final RoleRepository roleRepo;
  private final FileService fileService;

  private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  public UserServiceImpl(UserRepository userRepo,
                         RoleRepository roleRepo,
                         @Qualifier("user-image-file-service") FileService fileService) {
    this.userRepo = userRepo;
    this.roleRepo = roleRepo;
    this.fileService = fileService;
  }

  @Override
  public User getUser(String username) {
    logger.info("Fetching User {}", username);
    return userRepo.findByUsername(username)
      .orElseThrow(UserNotFoundException::new);
  }

  @Override
  public User getUser(long id) {
    logger.info("Fetching User id: {}", id);
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
    logger.info("Updating User {}", oldUsername);
    // check if username is new
    if (!oldUsername.equals(user.getUsername())
    // and check if it's available
      && userRepo.findByUsername(user.getUsername()).isPresent()) {
      throw new UsernameAlreadyExistsException("Username unavailable.");
    }
    User toUpdate = userRepo.findByUsername(oldUsername)
      .orElseThrow(UserNotFoundException::new);
    toUpdate.update(user);
    updateSecurityContext(toUpdate.getUsername());
    return userRepo.save(toUpdate);
  }

  private void updateSecurityContext(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User userDetails = (User) authentication.getPrincipal();
    userDetails.setUsername(username);
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
    User toUpdate = userRepo.findByUsername(username).orElseThrow(UserNotFoundException::new);
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
    User toUpdate = userRepo.findByUsername(username).orElseThrow(UserNotFoundException::new);
    String imageFileName = StringUtil.getUserImageFileName(toUpdate.getId(), imageFile.getContentType());

    if (!toUpdate.getImage().equals(imageFileName)) {
      toUpdate.setImage(imageFileName);
      userRepo.save(toUpdate);
    }
    fileService.saveFile(imageFile, imageFileName);
  }


  @Override
  public User add(User user) {
    if (userRepo.findByUsername(user.getUsername()).isPresent()) {
      throw new UsernameAlreadyExistsException("Username not available");
    }
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    user.setPassword(encoder.encode(user.getPassword()));
    user.setCreationDate(LocalDate.now());
    user.setActive(true);
    user.addRole(roleRepo.findByNameIgnoreCase("USER"));
    user.setImage(DEFAULT_PROFILE_IMAGE);

    return userRepo.save(user);
  }
}
