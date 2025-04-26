package com.example.MiniEvent.adapter.web.controller.user;

import com.example.MiniEvent.usecase.inteface.GetUserInfoUseCase;
import com.example.MiniEvent.usecase.inteface.RegisterUserUseCase;
import com.example.MiniEvent.adapter.web.dto.request.RegisterRequest;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.adapter.web.response.ResponseObject;
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
    private final GetUserInfoUseCase getUserInfoUseCase;

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

    @GetMapping("/info")
    public ResponseEntity<?> getInfo(@RequestHeader("Authorization") String authHeader) {
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );


        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get info user successfully")
                        .data(user)
                        .build()
        );
    }

    @GetMapping("/test")
    public ResponseEntity<?> getTest() {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get info user successfully")
                        .data(null)
                        .build()
        );
    }

}
