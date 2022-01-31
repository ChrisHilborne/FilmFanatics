package io.chilborne.filmfanatic.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionControllerAdvice {

  private final Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

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
