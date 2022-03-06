package io.chilborne.filmfanatic.controller.api;

import io.chilborne.filmfanatic.exception.UnauthorizedException;
import io.chilborne.filmfanatic.security.jwt.JwtRequest;
import io.chilborne.filmfanatic.security.jwt.JwtResponse;
import io.chilborne.filmfanatic.security.jwt.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Slf4j
public class ApiAuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final UserDetailsService userDetailsService;
  private final JwtTokenUtil jwtTokenUtil;

  public ApiAuthenticationController(AuthenticationManager authenticationManager,
                                     UserDetailsService userDetailsService,
                                     JwtTokenUtil jwtTokenUtil)
  {
    this.authenticationManager = authenticationManager;
    this.userDetailsService = userDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping(path = "/auth", consumes = "application/json", produces = "application/json")
  public ResponseEntity<?> login(@RequestBody JwtRequest authRequest) throws Exception
  {
    log.info("BEGIN login for username: {}", authRequest.getUsername());
    authenticate(authRequest.getUsername(), authRequest.getPassword());
    final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
    final String jwtToken = jwtTokenUtil.generateToken(userDetails);
    log.info("SUCCESS login:: token - {}", jwtToken);
    return ResponseEntity.ok(new JwtResponse(jwtToken));
  }

  private void authenticate(String username, String password) throws Exception
  {
    try {
      UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
      authenticationManager.authenticate(authenticationToken);
    } catch (DisabledException de) {
      throw new RuntimeException("User disabled", de);
    } catch (BadCredentialsException bce) {
      throw new UnauthorizedException("Bad credentials", bce);
    }
  }
}
