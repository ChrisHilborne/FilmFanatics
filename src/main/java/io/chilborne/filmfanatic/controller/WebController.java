package io.chilborne.filmfanatic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {

  private final Logger logger = LoggerFactory.getLogger(WebController.class);

  @RequestMapping(path = "/*", method = {RequestMethod.GET, RequestMethod.POST})
  public String index(Model model) {
    logger.info("Connection... serving index.html");
    return "index";
  }

  @RequestMapping(path = "/error-test", method = {RequestMethod.GET})
  public String errorTest(Model model) throws RuntimeException {
    throw new RuntimeException("This is a test Exception");
  }

  @ExceptionHandler
  public ModelAndView handleException(HttpServletRequest request, Exception exception){
    logger.error("Exception Caught!", exception);
    ModelAndView mav = new ModelAndView();
    mav.addObject("message", exception.getMessage());
    mav.addObject("exception", exception);
    mav.setViewName("error");
    return mav;
  }
}
