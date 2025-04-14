package com.example.MiniEvent.service.user;

import com.example.MiniEvent.DTO.request.RegisterDTO;
import com.example.MiniEvent.model.AppUser;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final Firestore firestore;

    public AppUser register(RegisterDTO request) throws FirebaseAuthException, ExecutionException, InterruptedException {
        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setPassword(request.getPassword());

        AppUser user = AppUser.builder()
                .id(FirebaseAuth.getInstance().createUser(createRequest).getUid())
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .build();

        firestore.collection("users").document(user.getId()).set(user).get();
        return user;
    }
}
