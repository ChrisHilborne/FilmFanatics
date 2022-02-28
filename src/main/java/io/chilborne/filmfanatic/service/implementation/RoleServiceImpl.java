package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.domain.Role;
import io.chilborne.filmfanatic.repository.RoleRepository;
import io.chilborne.filmfanatic.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;

  public RoleServiceImpl(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  @Override
  public List<Role> getAllRoles() {
    return roleRepository.findAll();
  }
}
