package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.Film;
import io.chilborne.filmfanatic.domain.Score;
import io.chilborne.filmfanatic.domain.User;
import io.chilborne.filmfanatic.service.FilmService;
import io.chilborne.filmfanatic.service.PersonService;
import io.chilborne.filmfanatic.service.UserService;
import io.chilborne.filmfanatic.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.security.Principal;
import java.util.Optional;

import static io.chilborne.filmfanatic.domain.PersonTypeEnum.*;

@Controller
@Slf4j
public class FilmController {

  private final FilmService filmService;
  private final UserService userService;
  private final PersonService personService;

  public FilmController(FilmService filmService, UserService userService, PersonService personService) {
    this.filmService = filmService;
    this.userService = userService;
    this.personService = personService;
  }

  @RequestMapping(path = "films/{filmUrl}", method = RequestMethod.GET)
  public String filmInfo(@PathVariable("filmUrl") String filmUrl,
                         Model model,
                         Authentication authentication) {
    Film film = filmService.getFilmByUrl(filmUrl);
    model.addAttribute("film", film);

    if (authentication != null) {
      User authenticatedUser = (User) authentication.getPrincipal();
      Optional<Score> userScore = film.getScores()
        .stream()
        .filter(scr -> scr.getUser().getId() == authenticatedUser.getId())
        .findFirst();
      if (userScore.isPresent()) {
        model.addAttribute("score", userScore.get());
      } else {
        model.addAttribute("newScore", new Score());
      }
    }
    return "film-info";
  }

  @RequestMapping(path = "films/{filmUrl}/score", method = RequestMethod.POST)
  public String filmInfo(@PathVariable("filmUrl") String filmUrl,
                         @ModelAttribute("newScore") @Valid Score score,
                         BindingResult result,
                         Authentication auth) {
    if (result.hasErrors()) {
      return "film-info";
    }
    score.setUser((User) auth.getPrincipal());
    filmService.addScore(filmUrl, score);

    return "redirect: /films/" + filmUrl;
  }

  @RequestMapping(path = "films/add", method = RequestMethod.GET)
  public String createFilm(Model model) {
    Film newFilm = new Film();

    model.addAttribute("film", newFilm);
    model.addAttribute("directors", personService.getPeopleByType(DIRECTOR));
    model.addAttribute("actors", personService.getPeopleByType(ACTOR));
    model.addAttribute("screenwriters", personService.getPeopleByType(SCREEN_WRITER));
    model.addAttribute("cinematographers", personService.getPeopleByType(CINEMATOGRAPHER));
    model.addAttribute("composers", personService.getPeopleByType(COMPOSER));
    return "create-film";
  }

  @RequestMapping(path = "films/add", method = RequestMethod.POST)
  public String createFilm(@RequestParam("posterImage") MultipartFile posterImage,
                           @ModelAttribute("film") @Valid Film film,
                           BindingResult result,
                           Model model,
                           Principal principal)
  {
    if (result.hasErrors()) {
      return "create-film";
    }
    else {
      film.setUser(userService.getUser(principal.getName()));
      Film createdFilm = filmService.addFilm(film);
      if (!posterImage.isEmpty()) {
        createdFilm = filmService.savePoster(film, posterImage);
      }
      model.addAttribute("film", createdFilm);
      return "redirect: /films/" + StringUtil.getFilmUrl(createdFilm.getTitle(), createdFilm.getYear());
    }
  }
}
