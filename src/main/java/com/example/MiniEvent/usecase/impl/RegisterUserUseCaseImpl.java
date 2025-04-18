package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.repository.UserRepository;
import com.example.MiniEvent.usecase.inteface.RegisterUserUseCase;
import com.example.MiniEvent.web.dto.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;

    @Override
    public AppUser register(RegisterRequest request) {

        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (request.getUsername() == null || request.getUsername().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        return userRepository.save(request);
    }
}
