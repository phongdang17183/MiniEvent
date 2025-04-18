package com.example.MiniEvent.usecase.inteface;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.web.DTO.request.RegisterDTO;
import com.google.firebase.auth.FirebaseAuthException;

import java.util.concurrent.ExecutionException;

public interface RegisterUserUseCase {
    AppUser register(RegisterDTO request) throws FirebaseAuthException, ExecutionException, InterruptedException;
}
