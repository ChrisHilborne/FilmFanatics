package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.PersonTypeEnum;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.FilmService;
import io.chilborne.filmfanatic.service.PersonService;
import io.chilborne.filmfanatic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import static io.chilborne.filmfanatic.domain.PersonTypeEnum.*;

@Controller
@Slf4j
public class FilmController {

  private final FilmService filmService;
  private final PersonService personService;

  public FilmController(FilmService filmService, PersonService personService) {
    this.filmService = filmService;
    this.personService = personService;
  }

  @RequestMapping(path = "films/add", method = RequestMethod.GET)
  public String createFilm(Model model, Authentication authentication) {
    Film newFilm = new Film();
    if (authentication != null) {
      newFilm.setUser((User) authentication.getPrincipal());
    }

    model.addAttribute("film", newFilm);
    model.addAttribute("directors", personService.getPeopleByType(DIRECTOR));
    model.addAttribute("actors", personService.getPeopleByType(ACTOR));
    model.addAttribute("screenwriters", personService.getPeopleByType(SCREEN_WRITER));
    model.addAttribute("cinematographers", personService.getPeopleByType(CINEMATOGRAPHER));
    model.addAttribute("composers", personService.getPeopleByType(COMPOSER));
    return "create-film";
  }

  @RequestMapping(path = "films/add", method = RequestMethod.POST)
  public String createFilm(@Valid @ModelAttribute("film") Film film,
                           BindingResult result,
                           @ModelAttribute("posterImage") MultipartFile posterImage,
                           Model model,
                           Authentication authentication)
  {
    if (result.hasErrors()) {
      return "create-film";
    }
    else {
      Film createdFilm = filmService.addFilm(film);
      if (posterImage != null) {
        createdFilm = filmService.savePoster(film, posterImage);
      }
      model.addAttribute("film", createdFilm);
      return "redirect: films/" + StringUtil.getFilmUrl(createdFilm.getTitle(), createdFilm.getYear());
    }
  }
}
