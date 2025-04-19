package com.example.MiniEvent.service.inteface;

import com.example.MiniEvent.web.exception.ImageUploadException;
import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String uploadImage(MultipartFile image);
}
