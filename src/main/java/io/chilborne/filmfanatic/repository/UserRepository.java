package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUsername();

}
