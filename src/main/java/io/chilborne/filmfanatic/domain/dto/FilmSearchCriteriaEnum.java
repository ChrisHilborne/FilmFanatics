package io.chilborne.filmfanatic.domain.dto;

public enum FilmSearchCriteriaEnum {
  TITLE("Title"),
  YEAR("Year"),
  MAX_DURATION("Max Length"),
  AVG_SCORE("Average Score"),
  ACTOR("Actor"),
  DIRECTOR("Director"),
  SCREENWRITER("Screenwriter"),
  CINEMATOGRAPHER("Cinematographer"),
  COMPOSER("Composer");


  private final String displayValue;

  FilmSearchCriteriaEnum(String displayValue) {
    this.displayValue = displayValue;
  }

  public String getDisplayValue() {
    return displayValue;
  }
}
