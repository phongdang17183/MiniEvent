package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.web.dto.request.RegisterRequest;
import org.springframework.web.multipart.MultipartFile;

public interface RegisterUserUseCase {
    AppUser register(RegisterRequest request, MultipartFile image);
}
