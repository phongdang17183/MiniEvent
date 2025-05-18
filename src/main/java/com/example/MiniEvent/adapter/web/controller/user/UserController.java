package com.example.MiniEvent.adapter.web.controller.user;

import com.example.MiniEvent.adapter.web.dto.request.LoginRequest;
import com.example.MiniEvent.usecase.inteface.*;
import com.example.MiniEvent.adapter.web.dto.request.RegisterRequest;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.adapter.web.response.ResponseObject;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final GetUserInfoUseCase getUserInfoUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUserByPhoneUseCase getUserByPhoneUseCase;
    private final UpdateUserInEvent updateUserInEvent;
    private final DeleteAccountUseCase deleteAccountUseCase;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {
        AppUser user = registerUserUseCase.register(request);
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Get login info successfully")
                        .data(loginUserUseCase.login(loginRequest))
                        .build()
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> getUserByPhone(
            @Valid @RequestParam String phone,
            @RequestParam(required = false) String lastDate) {
        Instant cursor = Instant.parse(lastDate);
        List<AppUser> users = getUserByPhoneUseCase.findAllByPhoneAfter(phone, cursor);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("List users search by phone")
                        .data(users)
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

    @PostMapping("/events/{eventId}/add")
    public ResponseEntity<?> addUserInEvent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String eventId,
            @RequestParam String phoneNumber
    ) {
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Add user to event successfully")
                        .data(updateUserInEvent.addUserInEvent(user.getId(), eventId, phoneNumber))
                        .build()
        );
    }

    @DeleteMapping("/events/{eventId}/remove")
    public ResponseEntity<?> removeUserInEvent(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String eventId,
            @RequestParam String phoneNumber
    ) {
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Add user to event successfully")
                        .data(updateUserInEvent.removeUserInEvent(user.getId(), eventId, phoneNumber))
                        .build()
        );
    }

    @DeleteMapping("/info")
    public ResponseEntity<?> removeUserInEvent(
            @RequestHeader("Authorization") String authHeader
    ) {
        String idToken = authHeader.replace("Bearer ", "");
        AppUser user = getUserInfoUseCase.getInfo(idToken).orElseThrow(
                () -> new DataNotFoundException("User not found", HttpStatus.NOT_FOUND)
        );
        deleteAccountUseCase.deleteAccount(user.getId());
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.builder()
                        .status(HttpStatus.OK.value())
                        .message("Delete user with all related record successfully")
                        .data(null)
                        .build()
        );
    }

}
