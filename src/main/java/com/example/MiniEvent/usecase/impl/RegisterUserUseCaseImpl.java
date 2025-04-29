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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;
    private final AuthService authService;
    @Qualifier("dicebearApiUrl")
    private final WebClient dicebearWebClient;

    @Override
    public AppUser register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        String randomSeed = request.getUsername() + "-" + UUID.randomUUID();
        byte[] avatarByte = dicebearWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("seed", randomSeed)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();


        String imageUrl = imageStorageService.uploadImage(avatarByte);

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
