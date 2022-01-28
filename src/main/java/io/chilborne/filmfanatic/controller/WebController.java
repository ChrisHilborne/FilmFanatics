package io.chilborne.filmfanatic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

  @GetMapping("/")
  @PostMapping("/")
  public String index(Model model) {
    return "index";
  }
}
