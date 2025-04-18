package com.example.MiniEvent.service.impl;

import com.cloudinary.Cloudinary;
import com.example.MiniEvent.service.usecase.ImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageStorageService implements ImageStorageService {

    private final Cloudinary cloudinary;

    private final Map<String, Boolean> options = Map.of(
            "use_filename", true,
            "unique_filename", false,
            "overwrite", true
    );

    @Override
    public String uploadImage(MultipartFile image) throws Exception {
        try {
            Map result = cloudinary.uploader().upload(image.getBytes(), options);
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }
}
