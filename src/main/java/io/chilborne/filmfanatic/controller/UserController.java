package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.dto.ChangePasswordDTO;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.domain.dto.EditUserDTO;
import io.chilborne.filmfanatic.domain.dto.CreateUserDTO;
import io.chilborne.filmfanatic.exception.UnauthorizedException;
import io.chilborne.filmfanatic.service.RoleService;
import io.chilborne.filmfanatic.service.UserService;
import org.apache.tomcat.util.http.parser.Authorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

  private final UserService userService;
  private final RoleService roleService;
  private final Logger logger = LoggerFactory.getLogger(UserController.class);

  public UserController(UserService userService, RoleService roleService) {
    this.userService = userService;
    this.roleService = roleService;
  }

  @RequestMapping(path = "/login", method = RequestMethod.GET)
  public String login(Model model) {
    return "login";
  }

  @RequestMapping(path = "/register", method = RequestMethod.GET)
  public String registerUser(Model model) {
    model.addAttribute("createUserDTO", new CreateUserDTO());
    model.addAttribute("roles", roleService.getAllRoles());
    return "register";
  }

  @RequestMapping(path = "/register", method = RequestMethod.POST)
  public String registerNewUser(@RequestParam("userImage")MultipartFile imageFile,
                                @ModelAttribute @Valid CreateUserDTO createUserDTO,
                                BindingResult result,
                                Authentication auth)
  {
    if (result.hasErrors()) {
      return "register";
    }
    User newUser = userService.add(createUserDTO.buildUser());
    if (!imageFile.isEmpty()) {
      userService.saveUserImage(newUser.getUsername(), imageFile);
    }
    if (auth != null &&
      auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
      return "redirect:/profile/" + newUser.getUsername() + "?user=created";
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

  @RequestMapping(path = "/profile/{username}", method = RequestMethod.GET)
  public String profile(@PathVariable("username") String username,
                        @RequestParam(name = "user", required = false) String requestParam,
                        Model model,
                        Authentication auth)
  {
    if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
      throw new UnauthorizedException();
    }
    User user = userService.getUser(username);
    model.addAttribute("user", user);
    return "profile";
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
    model.addAttribute("editUserDTO", new EditUserDTO(user));
    model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
    return "edit-profile";
  }

  @RequestMapping(path = "/user/edit", method = RequestMethod.POST)
  public String updateUser(@Valid @ModelAttribute("editUserDTO") EditUserDTO editUserDTO,
                           BindingResult result,
                           Model model,
                           Principal principal)
  {
    if (result.hasErrors()) {
      model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
      return "edit-profile";
    }
    else {
      String loggedInUsername = principal.getName();
      logger.info("Updating {} to {}", loggedInUsername, editUserDTO);
      User updated = userService.updateUser(loggedInUsername, editUserDTO.map());
      model.addAttribute("user", updated);
      return "redirect:/profile/";
    }
  }

  @RequestMapping(path = "/user/change-password", method = RequestMethod.POST)
  public String changePassword(@Valid @ModelAttribute("changePasswordDTO") ChangePasswordDTO changePassword,
                               BindingResult result,
                               Model model,
                               Authentication authentication)
  {
    if (result.hasErrors()) {
      model.addAttribute("editUserDTO", new EditUserDTO((User) authentication.getPrincipal()));
      model.addAttribute("error", true);
      return "edit-profile";
    }
    logger.info("Changing Password for {}", authentication.getName());
    userService.changePassword(authentication.getName(), changePassword.getOldPassword(), changePassword.getPassword());
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
