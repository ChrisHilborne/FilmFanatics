package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/login")
  public String login(Model model) {
    return "login";
  }

  @GetMapping("/registration")
  public String registerUser(Model model) {
    model.addAttribute("user", new User());
    return "registration";
  }

  @GetMapping("/profile/{username}")
  public String profile(Model model, HttpServletRequest request, @PathVariable String username) {
    model.addAttribute("user", userService.getUser(username));
    return "profile";
  }
}
