package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.dto.ChangePasswordForm;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.domain.dto.UserApplicationForm;
import io.chilborne.filmfanatic.exception.UnauthorizedException;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
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

  @RequestMapping(path = "/register", method = RequestMethod.GET)
  public String registerUser(Model model) {
    model.addAttribute("userApplicationForm", new UserApplicationForm());
    return "register";
  }

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public String registerNewUser(@RequestParam("userImage")MultipartFile imageFile,
                                @ModelAttribute @Valid UserApplicationForm userApplicationForm,
                                BindingResult result)
  {
    if (result.hasErrors()) {
      return "register";
    }
    User newUser = userService.add(userApplicationForm.buildUser());
    if (!imageFile.isEmpty()) {
      userService.saveUserImage(newUser.getUsername(), imageFile);
    }
    return "login";
  }

  @RequestMapping(path = "/profile", method = RequestMethod.GET)
  public String profile(Model model, Principal principal) {
    if (principal != null) {
      User user = userService.getUser(principal.getName());
      model.addAttribute("user", user);
      return "profile";
    }
    else return "/";
  }

  @RequestMapping(path = "/user/image", method = RequestMethod.POST)
  public String userImage(@RequestParam("userImage")MultipartFile imageFile, Principal principal) {
    logger.info("Saving User {} userImage", principal.getName());
    userService.saveUserImage(principal.getName(), imageFile);
    return "redirect:/profile/edit";
  }


  @RequestMapping(path = "/profile/edit", method = RequestMethod.GET)
  public String editProfile(Model model, Principal principal) {
    User user = userService.getUser(principal.getName());
    model.addAttribute("user", user);
    model.addAttribute("changePassword", new ChangePasswordForm());
    return "edit-profile";
  }

  @RequestMapping(path = "/user/edit", method = RequestMethod.POST)
  public String updateUser(@ModelAttribute User user, Model model, Principal principal) {
    String loggedInUsername = principal.getName();
    logger.info("Updating {} to {}", loggedInUsername, user);
    User updated = userService.updateUser(loggedInUsername, user);
    model.addAttribute("user", updated);
    return "redirect:/profile/";
  }

  @RequestMapping(path = "/user/change-password", method = RequestMethod.POST)
  public String changePassword(@Valid @ModelAttribute("changePassword") ChangePasswordForm changePassword,
                               BindingResult result,
                               Model model,
                               Principal principal)
  {
    if (result.hasErrors()) {
      model.addAttribute("user", userService.getUser(principal.getName()));
      model.addAttribute("error", true);
      return "edit-profile";
    }
    logger.info("Changing Password for {}", principal.getName());
    userService.changePassword(principal.getName(), changePassword.getOldPassword(), changePassword.getPassword());
    return "redirect:/profile/edit";
  }

  @RequestMapping(path = "/profile/delete", method = RequestMethod.GET)
  public String delete(Model model) {
    return "delete-user";
  }

  @RequestMapping(path = "/user/delete", method = {RequestMethod.GET})
  public String deleteUser(Principal principal) throws UnauthorizedException {
    logger.info("Deleting User {}", principal.getName());
    userService.deleteUser(principal.getName());
    return "redirect:/logout";
  }

}
