package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.FilmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Slf4j
public class FilmController {

  private final FilmService filmService;

  public FilmController(FilmService filmService) {
    this.filmService = filmService;
  }

  @RequestMapping(path = "films/create", method = RequestMethod.GET)
  public String createFilm(Model model, Authentication authentication) {
    Film newFilm = new Film();
    newFilm.setUser((User) authentication.getPrincipal());
    model.addAttribute("film", newFilm);
    return "create-film";
  }

  @RequestMapping(path = "films/create", method = RequestMethod.POST)
  public String createFilm(@Valid @ModelAttribute("film") Film film,
                           BindingResult result,
                           Model model)
  {
    if (result.hasErrors()) {
      return "create-film";
    }
    else {
      Film createdFilm = filmService.addFilm(film);
      model.addAttribute("film", createdFilm);
      return "film-info";
    }
  }
}
