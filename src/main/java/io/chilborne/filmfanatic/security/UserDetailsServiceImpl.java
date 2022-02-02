package io.chilborne.filmfanatic.security;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.exception.UserNotFoundException;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {


  private final UserService userService;
  private final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

  public UserDetailsServiceImpl(UserService userService) {
    this.userService = userService;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    try {
      User user = userService.getUser(username);
      List<GrantedAuthority> authorities = getUserAuthorities(user);

      return buildUserDetails(user, authorities);

    } catch (UserNotFoundException e) {
      logger.error("User {} not found", username, e);
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }

  private UserDetails buildUserDetails(User user, List<GrantedAuthority> authorities) {
    return new org.springframework.security.core.userdetails.User(
      user.getUsername(), user.getPassword(), user.isActive(), true, true, true, authorities
    );
  }

  private List<GrantedAuthority> getUserAuthorities(User user) {
    Set<GrantedAuthority> authorities = new HashSet<>();
    user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
    return new ArrayList<>(authorities);
  }
}
