package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.adapter.web.dto.request.LoginRequest;

public interface LoginUserUseCase {
    Object login(LoginRequest loginRequest);
}
