package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserController {

  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(path = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    return "login";
  }

  @RequestMapping(path = "/registration", method = RequestMethod.GET)
  public String registerUser(Model model) {
    model.addAttribute("user", new User());
    return "registration";
  }

  @RequestMapping(path = "/profile/{username}", method = RequestMethod.GET)
  public String profile(Model model, HttpServletRequest request, @PathVariable String username) {
    User user = userService.getUser(username);
    model.addAttribute("user", userService.getUser(username));
    return "profile";
  }

  @RequestMapping(path = "/profile/edit", method = RequestMethod.GET)
  public String editProfile(Model model, Principal principal) {
    User user = userService.getUser(principal.getName());
    model.addAttribute("user", user);
    return "edit-profile";
  }

  @RequestMapping(path = "/user/edit", method = RequestMethod.PUT)
  public String updateUser(@ModelAttribute User user, Model model, Principal principal) {
    String loggedInUsername = principal.getName();
    logger.info("Updating {} to {}", loggedInUsername, user);
    User updated = userService.updateUser(loggedInUsername, user);
    model.addAttribute("user", updated);
    return "redirect:/profile/" + user.getUsername();
  }

  @RequestMapping(path = "/profile/delete", method = RequestMethod.GET)
  public String delete(Model model) {
    return "delete-user";
  }

  @RequestMapping(path = "/user/delete", method = RequestMethod.DELETE)
  public String deleteUser(Model model, Principal principal) {
    logger.info("Deleting User {}", principal.getName());
    userService.deleteUser(principal.getName());
    return "redirect:/logout";
  }

}
