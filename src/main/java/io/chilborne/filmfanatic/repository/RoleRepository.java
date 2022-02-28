package io.chilborne.filmfanatic.repository;

import io.chilborne.filmfanatic.domain.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface RoleRepository extends CrudRepository<Role, Long> {

  Role findByNameIgnoreCase(String name);

  List<Role> findAll();
}
