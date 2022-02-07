package io.chilborne.filmfanatic.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static io.chilborne.filmfanatic.util.Constants.*;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
  private final UserDetailsServiceImpl userDetailsService;

  public WebSecurityConfig(UserDetailsServiceImpl userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService)
      .passwordEncoder(passwordEncoder());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.
      authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers("/register").permitAll()
        .antMatchers("/login").permitAll()
        .antMatchers("/film/**").permitAll()
        .antMatchers("/films/**").permitAll()
        .antMatchers("/h2-console/**").permitAll()
        .antMatchers("/admin/**").hasAnyAuthority(ADMIN_ROLE)
      .anyRequest().authenticated()
    .and()
      .formLogin()
        .loginPage(LOGIN_URL)
        .defaultSuccessUrl(LOGIN_SUCCESS_URL, true)
        .failureUrl(LOGIN_FAILURE)
    .and()
      .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
        .logoutSuccessUrl(LOGOUT_SUCCESS_URL)
    .and()
      .csrf().disable()
      // in order to allow access to h2-console for testing
      // TODO remove before release
      .headers().frameOptions().disable();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers(
      "/resources/**",
      "/static/**",
      "/templates/**",
      "/images/**",
      "/styles/**",
      "/css/**",
      "/js/**",
      "/fonts/**",
      "/webjars/**"
    );
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(-1);
  }
}
