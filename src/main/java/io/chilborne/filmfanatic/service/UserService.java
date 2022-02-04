package io.chilborne.filmfanatic.service;

import io.chilborne.filmfanatic.domain.User;

import java.util.Set;

public interface UserService {

  User getUser(String username);

  User getUser(long id);

  Set<User> getAllUsers();

  User updateUser(String oldUsername, User user);
}
