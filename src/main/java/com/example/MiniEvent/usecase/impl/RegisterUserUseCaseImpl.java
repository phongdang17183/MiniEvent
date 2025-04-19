package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.usecase.inteface.RegisterUserUseCase;
import com.example.MiniEvent.adapter.web.dto.request.RegisterRequest;
import com.example.MiniEvent.adapter.web.exception.BadRequestException;
import com.google.cloud.Timestamp;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;
    private final AuthService authService;

    @Override
    public AppUser register(RegisterRequest request, MultipartFile image) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = imageStorageService.uploadImage(image);
        }

        AppUser user = AppUser.builder()
                .id(authService.createUser(request.getEmail(), request.getPassword()).getUid())
                .email(request.getEmail())
                .username(request.getUsername())
                .image(imageUrl)
                .phone(request.getPhone())
                .createDay(Timestamp.now())
                .eventCreate(0)
                .eventJoin(0)
                .build();
        return userRepository.save(user);

    }
}
