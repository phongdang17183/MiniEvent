package com.example.MiniEvent.service.usecase;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String uploadImage(MultipartFile image) throws Exception;
}
