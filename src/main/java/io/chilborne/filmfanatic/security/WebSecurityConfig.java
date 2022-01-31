package io.chilborne.filmfanatic.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.
      authorizeRequests()
      .antMatchers("/").permitAll()
      .antMatchers("/error").permitAll()
      .antMatchers("/h2-console/*").permitAll()
    .and()
      // in order to allow access to h2-console for testing
      // TODO remove before release
      .csrf().disable()
      .headers().frameOptions().disable();
  }
}
