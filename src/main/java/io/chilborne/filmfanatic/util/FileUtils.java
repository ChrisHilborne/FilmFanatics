package io.chilborne.filmfanatic.util;

import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class FileUtils {

  private static ResourceLoader resourceLoader;

  public FileUtils(ResourceLoader resourceLoader) {
    FileUtils.resourceLoader = resourceLoader;
  }

  public static Path getResourcePath(String directory, String fileName) throws IOException {
    Path path = Paths.get(resourceLoader.getResource("classpath:" + directory + File.separator + fileName).getURI());
    return path;
  }
}
