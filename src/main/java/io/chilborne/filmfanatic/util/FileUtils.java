package io.chilborne.filmfanatic.util;

import org.hibernate.query.criteria.internal.PathSource;
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

  private static Path getRootResourcePath() throws IOException {
    return Paths.get(resourceLoader.getResource("classpath:").getURI());
  }

  public static Path getResourcePath(String directory, String fileName) throws IOException {
    Path path = Paths.get(getRootResourcePath() + File.separator + directory + File.separator + fileName);
    return path;
  }
}
