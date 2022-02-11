package io.chilborne.filmfanatic.util;

import java.util.Locale;

public class StringUtil {

  public static String getUserImageFileName(long userId, String contentType) {
    return userId + getFileExtension(contentType);
  }

  private static String getFileExtension(String contentType) {
    return "." + contentType.split("/")[1];
  }


}
