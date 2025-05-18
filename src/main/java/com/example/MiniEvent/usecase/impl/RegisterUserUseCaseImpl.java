package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.model.entity.EmailDetail;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.service.inteface.EmailService;
import com.example.MiniEvent.service.inteface.ImageStorageService;
import com.example.MiniEvent.usecase.inteface.RegisterUserUseCase;
import com.example.MiniEvent.adapter.web.dto.request.RegisterRequest;
import com.example.MiniEvent.adapter.web.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.UUID;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {
    public RegisterUserUseCaseImpl(
            UserRepository userRepository,
            ImageStorageService imageStorageService,
            AuthService authService,
            @Qualifier("appPasswordMailService") EmailService emailService,
            @Qualifier("dicebearApiUrl") WebClient dicebearWebClient,
            @Value("${spring.mail.username}") String emailUsername) {
        this.userRepository = userRepository;
        this.imageStorageService = imageStorageService;
        this.authService = authService;
        this.emailService = emailService;
        this.dicebearWebClient = dicebearWebClient;
        this.emailUsername = emailUsername;
    }

    private final UserRepository userRepository;
    private final ImageStorageService imageStorageService;
    private final AuthService authService;
    private final EmailService emailService;
    private final WebClient dicebearWebClient;
    private final String emailUsername;

    @Override
    public AppUser register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists", HttpStatus.BAD_REQUEST);
        }

        String randomSeed = request.getUsername() + "-" + UUID.randomUUID();

        String imageUrl = imageStorageService.uploadImage(fetchAvatar(randomSeed));

        AppUser user = AppUser.builder()
                .id(authService.createUser(request.getEmail(), request.getPassword()).getUid())
                .email(request.getEmail())
                .username(request.getUsername())
                .image(imageUrl)
                .phone(request.getPhone())
                .createDay(Instant.now())
                .eventCreate(0)
                .eventJoin(0)
                .build();
        String subject = "Register MiniEvent successfully";
        String msgBody = "Thank fore register MiniEvent app, hope you have fun in here :))";
        EmailDetail emailDetail = new EmailDetail(request.getEmail(), emailUsername, subject, msgBody);
        emailService.sendEmail(emailDetail);
        return userRepository.save(user);
    }

    byte[] fetchAvatar(String randomSeed) {
        return dicebearWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("seed", randomSeed)
                        .build())
                .retrieve()
                .bodyToMono(byte[].class)
                .block();
    }

}