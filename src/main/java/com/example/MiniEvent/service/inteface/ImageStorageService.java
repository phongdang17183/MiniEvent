package com.example.MiniEvent.service.inteface;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {
    String uploadImage(MultipartFile image) throws Exception;
}
