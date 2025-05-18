package com.example.MiniEvent.service.impl;

import com.cloudinary.Cloudinary;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.adapter.web.exception.ImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageStorageService implements ImageStorageService {

    private final Cloudinary cloudinary;

    private final Map<String, Boolean> options = Map.of(
            "use_filename", true,
            "unique_filename", true,
            "overwrite", false
    );

    @Override
    public String uploadImage(MultipartFile image) {
        try {
            Map result = cloudinary.uploader().upload(image.getBytes(), options);
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new ImageUploadException("Failed to upload image to cloudinary " , HttpStatus.BAD_REQUEST, e);
        }
    }

    @Override
    public String uploadImage(byte[] imageBytes) {
        try {
            Map result = cloudinary.uploader().upload(imageBytes, options);
            return result.get("secure_url").toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload avatar image", e);
        }
    }
}
