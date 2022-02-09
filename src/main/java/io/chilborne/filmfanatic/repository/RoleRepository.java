package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Role findByNameIgnoreCase(String name);
}
