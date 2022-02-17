package io.chilborne.filmfanatic.controller;

import io.chilborne.filmfanatic.domain.Person;
import io.chilborne.filmfanatic.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@Slf4j
public class PersonController {

  private final PersonService service;

  public PersonController(PersonService service) {
    this.service = service;
  }

  @RequestMapping(path = "person/add", method = RequestMethod.GET)
  public String addPerson(Model model) {
    model.addAttribute("person", new Person());
    return "add-person";
  }
  @RequestMapping(path = "person/add", method = RequestMethod.POST)
  public String addPerson(@ModelAttribute @Valid Person person,
                          BindingResult result,
                          Model model)
  {
    if (result.hasErrors()) {
      return "add-person";
    }
    else {
      Person added = service.addPerson(person);
      model.addAttribute("success", true);
      return "add-person";
    }
  }
}
