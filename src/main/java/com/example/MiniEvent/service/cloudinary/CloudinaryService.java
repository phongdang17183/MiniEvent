package com.example.MiniEvent.service.cloudinary;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    private final Map<String, Boolean> options = Map.of(
            "use_filename", true,
            "unique_filename", false,
            "overwrite", true
    );

    public Map uploadImage(MultipartFile image) throws IOException {
        return cloudinary.uploader().upload(image.getBytes(), options);
    }
}
