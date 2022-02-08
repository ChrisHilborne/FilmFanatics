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
  public void saveUserImage(MultipartFile imageFile, String imageFileName) throws ImageUploadException {
    try {
      Path toCopy = FileUtils.getResourcePath(userImageUploadDirectory, imageFileName);
      Files.copy(imageFile.getInputStream(), toCopy, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      logger.error("Error saving file {}", imageFileName, e);
      throw new ImageUploadException("Image Upload Failed", e);
    }
  }

  @Override
  public void saveUserImage(MultipartFile imageFile, String newImageFileName, String oldImageFileName)
    throws ImageUploadException
  {
    try {
      // delete old image
      Path toDeletePath = FileUtils.getResourcePath(userImageUploadDirectory, oldImageFileName);
      System.out.println("To delete path: " + toDeletePath.toAbsolutePath());
      Files.deleteIfExists(toDeletePath);

      // save new image
      Path toSavePath = FileUtils.getResourcePath(userImageUploadDirectory, "");
      System.out.println("To save path: " + toSavePath.toAbsolutePath());
      File newImageFile = new File(toSavePath + File.separator + newImageFileName);
      imageFile.transferTo(newImageFile);
    } catch (IOException e) {
      throw new ImageUploadException("Image Upload Failed", e);
    }
  }

}
