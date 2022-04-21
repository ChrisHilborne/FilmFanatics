package io.chilborne.filmfanatic.config;

import groovy.util.logging.Slf4j;
import io.chilborne.filmfanatic.domain.Film;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
@EnableWebMvc
@Slf4j
public class ApplicationConfig implements WebMvcConfigurer {
  public static String uploadDirectory= System.getProperty("user.home") + File.separator + "/film-fanatics";

  public ApplicationConfig() {
    File upload = new File(uploadDirectory);
    if (!upload.exists()) {
      upload.mkdirs();
    }
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    //registry.addResourceHandler("/images/**").addResourceLocations("file:" + uploadDirectory + File.separator);
    registry.addResourceHandler("/images/**", "styles/css/**", "/js/**", "/webjars/**")
      .addResourceLocations("file:" + uploadDirectory + File.separator + "/images/", "classpath:/static/images/", "classpath:/static/styles/css/",
      "classpath:/static/js/", "/webjars/");

  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }



}
