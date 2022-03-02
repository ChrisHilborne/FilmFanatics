package io.chilborne.filmfanatic.security.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

  private final JwtTokenUtil tokenUtil;
  private final UserDetailsService userDetailsService;

  public JwtRequestFilter(JwtTokenUtil tokenUtil, UserDetailsService userDetailsService) {
    this.tokenUtil = tokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    final String requestToken = request.getHeader("Authorization");
    String username = null;
    String jwtToken = null;

    if (requestToken != null && requestToken.startsWith("Bearer ")) {
      jwtToken = requestToken.substring(7);
      try {
        username = tokenUtil.getUsername(jwtToken);
      } catch (IllegalArgumentException iae) {
        logger.error("Can't obtain the token", iae);
      } catch (ExpiredJwtException eje) {
        logger.error("Token has expired", eje);
      }
    }
    else {
        logger.warn("The token does not start with 'Bearer'");
    }
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = userDetailsService.loadUserByUsername(username);
      UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
          userDetails,
          null,
          userDetails.getAuthorities()
        );
      SecurityContextHolder.getContext().setAuthentication(authToken);

      filterChain.doFilter(request, response);
    }
  }
}
