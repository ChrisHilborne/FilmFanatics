package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.exception.ImageUploadException;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@Service("user-image-file-service")
public class UserImageFileService implements FileService {

  @Value("${images.upload.directory.user}")
  private String userImageUploadDirectory;
  @Autowired
  ResourceLoader resourceLoader;
  private final Logger logger = LoggerFactory.getLogger(UserImageFileService.class);

  @Override
  public void saveFile(MultipartFile file, String fileName) throws ImageUploadException {
    try (InputStream fileStream = file.getInputStream()) {
      Path imagePath = FileUtils.getResourcePath(userImageUploadDirectory, fileName);
      if (Files.exists(imagePath)) {
        Files.copy(fileStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
      }
      else {
        Files.write(imagePath, fileStream.readAllBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
      }
    } catch (IOException e) {
      logger.error("Error saving file {}", fileName, e);
      throw new ImageUploadException("Image Upload Failed", e);
    }
  }

  @Override
  public void deleteFile(String fileName) {
    try {
      Path toDelete = FileUtils.getResourcePath(userImageUploadDirectory, fileName);
      Files.deleteIfExists(toDelete);
    } catch (IOException e) {
      logger.error("Error when deleting image file {}", fileName);
      throw new ImageUploadException("Image Upload Failed", e);
    }
  }


}
