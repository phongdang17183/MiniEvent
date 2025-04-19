package com.example.MiniEvent.web.controller.user;

import com.example.MiniEvent.usecase.inteface.RegisterUserUseCase;
import com.example.MiniEvent.web.dto.request.RegisterRequest;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.web.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestPart(value = "info") RegisterRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        AppUser user = registerUserUseCase.register(request, image);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("User register successfully")
                        .data(user)
                        .build()

        );
    }
}
