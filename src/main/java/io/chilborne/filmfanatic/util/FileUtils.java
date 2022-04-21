package io.chilborne.filmfanatic.util;

import groovy.util.logging.Slf4j;
import org.hibernate.query.criteria.internal.PathSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.chilborne.filmfanatic.config.ApplicationConfig.uploadDirectory;

@Component
public class FileUtils {

  private static ResourceLoader resourceLoader;
  private static Logger log = LoggerFactory.getLogger(FileUtils.class);


  public FileUtils(ResourceLoader resourceLoader) {
    FileUtils.resourceLoader = resourceLoader;
  }

  private static Path getRootResourcePath() throws IOException {
    log.info("Getting Root Resource Path");
    return Paths.get(resourceLoader.getResource("classpath:").getURI());
  }

  public static Path getResourcePath(String directory, String fileName) throws IOException {
    log.info("Getting individual resource path");
    Path path = Paths.get(getRootResourcePath() + File.separator + directory + File.separator + fileName);
    return path;
  }

  public static Path getResourcePathJar(String directory, String fileName) throws IOException {
    String saveDirectory = uploadDirectory + File.separator + directory;
    File saveDir = new File(saveDirectory);
    if (!saveDir.exists())
    {
      saveDir.mkdirs();
    }

    return Path.of(saveDirectory + File.separator + fileName);
  }
}
