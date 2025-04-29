package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.adapter.web.dto.request.RegisterRequest;

public interface RegisterUserUseCase {
    AppUser register(RegisterRequest request);
}
