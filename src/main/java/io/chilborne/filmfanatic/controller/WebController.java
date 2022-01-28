package io.chilborne.filmfanatic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

  private final Logger logger = LoggerFactory.getLogger(WebController.class);

  @GetMapping("/")
  @PostMapping("/")
  public String index(Model model) {
    logger.info("Connection... serving index.html");
    return "index";
  }
}
