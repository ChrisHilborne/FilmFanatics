package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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

  @GetMapping("/profile/edit")
  public String editProfile(Model model, Principal principal) {
    User user = userService.getUser(principal.getName());
    model.addAttribute("user", user);
    return "edit-profile";
  }

  @PostMapping("/update-user")
  public String updateUser(@ModelAttribute User user, Model model, Principal principal) {
    User updated = userService.updateUser(principal.getName(), user);
    model.addAttribute("user", updated);
    return "profile";
  }

  @GetMapping("/profile/{username}")
  public String profile(Model model, HttpServletRequest request, @PathVariable String username) {
    User user = userService.getUser(username);
    model.addAttribute("user", userService.getUser(username));
    return "profile";
  }
}
