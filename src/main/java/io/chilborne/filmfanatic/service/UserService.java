package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.exception.UnauthorizedException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public interface UserService {

  User getUser(String username);

  User getUser(long id);

  Set<User> getAllUsers();

  User updateUser(String oldUsername, User user);

  void deleteUser(String username);

  void changePassword(String username, String oldPassword, String newPassword) throws UnauthorizedException;

  void saveUserImage(String username, MultipartFile imageFile);

  User add(User user);
}
