package com.example.MiniEvent.service.impl;

import com.example.MiniEvent.service.inteface.AuthService;
import com.example.MiniEvent.web.exception.UnauthorizedException;
import com.example.MiniEvent.web.exception.UserCreationException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FireBaseAuthService implements AuthService {

    private final FirebaseAuth firebaseAuth;

    @Override
    public UserRecord createUser(String email, String password) {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password);
        try {
            return firebaseAuth.createUser(createRequest);
        }
        catch (Exception e) {
            throw new UserCreationException("Failed to create user in Firebase", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }

    @Override
    public FirebaseToken verifyToken(String idToken) {
        try {
            return firebaseAuth.verifyIdToken(idToken);
        }
        catch (Exception e) {
            throw new UnauthorizedException("Invalid or expired Firebase token", HttpStatus.UNAUTHORIZED, e);
        }

    }

}
