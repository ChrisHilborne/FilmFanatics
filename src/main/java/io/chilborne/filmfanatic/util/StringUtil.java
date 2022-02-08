package io.chilborne.filmfanatic.util;

import java.util.Locale;

public class StringUtil {

  public static String getUserImageFileName(String username, String contentType) {
    String fileName = username
      .toLowerCase(Locale.ROOT).replace(' ', '-')
      + getFileExtension(contentType);
    return fileName;
  }

  private static String getFileExtension(String contentType) {
    return "." + contentType.split("/")[1];
  }


}
