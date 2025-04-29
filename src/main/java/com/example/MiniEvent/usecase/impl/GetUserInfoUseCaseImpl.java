package com.example.MiniEvent.usecase.impl;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.adapter.repository.UserRepository;
import com.example.MiniEvent.model.entity.DecodedTokenInfo;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.usecase.inteface.GetUserInfoUseCase;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GetUserInfoUseCaseImpl implements GetUserInfoUseCase {

    private final UserRepository userRepository;
    private final AuthService authService;

    @Override
    public Optional<AppUser> getInfo(String idToken) {
        DecodedTokenInfo decodedToken = authService.verifyToken(idToken);
        String uid = decodedToken.getUid();

        return userRepository.findByUid(uid);
    }
}
