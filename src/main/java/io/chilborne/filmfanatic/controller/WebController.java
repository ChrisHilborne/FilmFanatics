package io.chilborne.filmfanatic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

  private final Logger logger = LoggerFactory.getLogger(WebController.class);

  @RequestMapping(path = "/*", method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model) {
    logger.info("Connection... serving index.html");
    return "index";
  }
}
