package com.example.MiniEvent.service.impl;

import com.example.MiniEvent.config.firebase.FireBaseProperties;
import com.example.MiniEvent.model.entity.AuthenticatedUser;
import com.example.MiniEvent.model.entity.DecodedTokenInfo;
import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.adapter.web.exception.UnauthorizedException;
import com.example.MiniEvent.adapter.web.exception.UserCreationException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class FireBaseAuthService implements AuthService {

    private final FirebaseAuth firebaseAuth;
    private final FireBaseProperties fireBaseProperties;
    private final WebClient webClient;

    @Override
    public AuthenticatedUser createUser(String email, String password) {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        try {
            UserRecord userRecord = firebaseAuth.createUser(createRequest);
            return new AuthenticatedUser(
                    userRecord.getUid(),
                    userRecord.getEmail()
            );
        }
        catch (Exception e) {
            throw new UserCreationException("Failed to create user in Firebase", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public DecodedTokenInfo verifyToken(String idToken) {
        try {
            FirebaseToken token = firebaseAuth.verifyIdToken(idToken);
            return new DecodedTokenInfo(
                    token.getUid(),
                    token.getEmail(),
                    token.getIssuer()
            );
        }
        catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired Firebase token", HttpStatus.UNAUTHORIZED, e);
        }
    }

    @Override
    public Object login(String email, String password) {
        Map<String, Object> requestBody = Map.of(
                "email", email,
                "password", password,
                "returnSecureToken", true
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/accounts:signInWithPassword")
                        .queryParam("key", fireBaseProperties.getApiKey())
                        .build())
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(Map.class)
                .block();
    }
}
