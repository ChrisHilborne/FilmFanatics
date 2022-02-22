package io.chilborne.filmfanatic.util;

import java.util.Locale;

public class StringUtil {

  public static String getUserImageFileName(long userId, String contentType) {
    return userId + getFileExtension(contentType);
  }

  private static String getFileExtension(String contentType) {
    return "." + contentType.split("/")[1];
  }


  public static String getFilmPosterFilename(String filmTitle, int filmYear, String contentType) {
    return getFilmUrl(filmTitle, filmYear) + getFileExtension(contentType);
  }

  public static String getFilmUrl(String filmTitle, int filmYear) {
    return String.join("-", filmTitle.toLowerCase(Locale.ROOT).split(" ")) + filmYear;
  }

}
