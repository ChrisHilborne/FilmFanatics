package io.chilborne.filmfanatic.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

  void saveUserImage(MultipartFile imageFile, String imageFileName);

  void saveUserImage(MultipartFile imageFile, String imageFileName, String oldImageFileName);
}
