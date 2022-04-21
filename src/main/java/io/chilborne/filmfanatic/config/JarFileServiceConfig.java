package io.chilborne.filmfanatic.config;

import groovy.util.logging.Slf4j;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.service.implementation.FileServiceImpl;
import io.chilborne.filmfanatic.service.implementation.JarFileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "jar", havingValue = "true")
public class JarFileServiceConfig {

  @Value("${images.upload.directory.user}")
  private String userImageUploadDirectory;
  @Value("${images.upload.directory.film}")
  private String filmPosterUploadDirectory;

  @Bean("user-image-file-service")
  public FileService userImageFileService() {
    return new JarFileServiceImpl(userImageUploadDirectory);
  }

  @Bean("film-poster-file-service")
  public FileService filmPosterFileService() {
    return new JarFileServiceImpl(filmPosterUploadDirectory);
  }

}
