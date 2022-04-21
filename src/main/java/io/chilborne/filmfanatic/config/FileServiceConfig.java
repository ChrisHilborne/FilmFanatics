package io.chilborne.filmfanatic.config;

import io.chilborne.filmfanatic.NotJarCondition;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.service.implementation.FileServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@Conditional(NotJarCondition.class)
public class FileServiceConfig {

  @Value("${images.upload.directory.user}")
  private String userImageUploadDirectory;
  @Value("${images.upload.directory.film}")
  private String filmPosterUploadDirectory;

  @Bean("user-image-file-service")
  public FileService userImageFileService() {
    return new FileServiceImpl(userImageUploadDirectory);
  }

  @Bean("film-poster-file-service")
  public FileService filmPosterFileService() {
    return new FileServiceImpl(filmPosterUploadDirectory);
  }

}
