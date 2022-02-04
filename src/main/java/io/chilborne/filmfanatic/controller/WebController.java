package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

  private final Logger logger = LoggerFactory.getLogger(WebController.class);
  private final UserService userService;

  public WebController(UserService userService) {
    this.userService = userService;
  }

  @RequestMapping(path = "/*", method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model, @CurrentSecurityContext(expression="authentication?.name")
    String username) {
    logger.info("Connection... serving index.html");
    if (username != null) {
      String userImage = userService.getUser(username).getImage();
      model.addAttribute("userImage", userImage);
      model.addAttribute("username", username);
    }
    return "index";
  }

  @RequestMapping(path = "/error-test", method = {RequestMethod.GET})
  public String errorTest(Model model) throws RuntimeException {
    throw new RuntimeException("This is a test Exception");
  }
}
