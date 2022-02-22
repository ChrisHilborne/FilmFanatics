package io.chilborne.filmfanatic.service.implementation;

import io.chilborne.filmfanatic.exception.ImageUploadException;
import io.chilborne.filmfanatic.service.FileService;
import io.chilborne.filmfanatic.util.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.*;

@Slf4j
public class FileServiceImpl implements FileService {

  private String uploadDirectory;

  public FileServiceImpl(String uploadDirectory) {
    this.uploadDirectory = uploadDirectory;
  }

  @Override
  public void saveFile(MultipartFile file, String fileName) throws ImageUploadException {
    log.info("Saving file {} as {}", file, fileName);
    try (InputStream fileStream = file.getInputStream()) {
      Path imagePath = FileUtils.getResourcePath(uploadDirectory, fileName);
      if (Files.exists(imagePath)) {
        Files.copy(fileStream, imagePath, StandardCopyOption.REPLACE_EXISTING);
      }
      else {
        Files.write(imagePath, fileStream.readAllBytes(), StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
      }
    } catch (IOException e) {
      log.error("Error saving file {}", fileName, e);
      throw new ImageUploadException("Image Upload Failed", e);
    }
  }

  @Override
  public void deleteFile(String fileName) {
    log.info("Deleting file {}", fileName);
    try {
      Path toDelete = FileUtils.getResourcePath(uploadDirectory, fileName);
      Files.deleteIfExists(toDelete);
    } catch (IOException e) {
      log.error("Error when deleting image file {}", fileName);
      throw new ImageUploadException("Image Upload Failed", e);
    }
  }


}
