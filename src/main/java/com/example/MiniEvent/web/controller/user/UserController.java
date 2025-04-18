package com.example.MiniEvent.web.controller.user;

import com.example.MiniEvent.usecase.inteface.RegisterUserUseCase;
import com.example.MiniEvent.web.DTO.request.RegisterDTO;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.web.response.ResponseObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) throws Exception{
        AppUser user = registerUserUseCase.register(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("User register successfully")
                        .data(user)
                        .build()

        );
    }
}
