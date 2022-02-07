package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUsernameIgnoreCase(String username);

  Set<User> findByActive(boolean active);

  void deleteByUsername(String username);

}
