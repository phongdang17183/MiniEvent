package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.adapter.web.dto.request.LoginRequest;
import com.example.MiniEvent.service.impl.FireBaseAuthService;
import com.example.MiniEvent.usecase.inteface.LoginUserUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final FireBaseAuthService firebaseAuthService;

    @Override
    public Object login(LoginRequest loginRequest) {
        return firebaseAuthService.login(loginRequest.getEmail(), loginRequest.getPassword());
    }
}
