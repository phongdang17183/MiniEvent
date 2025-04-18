package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.web.DTO.request.RegisterDTO;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FireBaseUserRepository implements UserRepository{
    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Override
    public AppUser save(RegisterDTO request) {
        try {
            UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                    .setEmail(request.getEmail())
                    .setPassword(request.getPassword());
            UserRecord firebaseUser = firebaseAuth.createUser(createRequest);

            AppUser user = AppUser.builder()
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .id(firebaseUser.getUid())
                    .build();

            firestore.collection("users").document(user.getId()).set(user).get();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AppUser> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection("users").document(id).get().get();
            if (!snapshot.exists()) {
                return Optional.empty();
            }
            return Optional.ofNullable(snapshot.toObject(AppUser.class));
        } catch (Exception e) {
            throw new RuntimeException("Firestore query failed", e);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            firebaseAuth.getUserByEmail(email);
            return true;
        } catch (FirebaseAuthException e) {
            if (e.getAuthErrorCode() == AuthErrorCode.USER_NOT_FOUND) {
                return false;
            }
            throw new RuntimeException("Firebase error while checking email: " + e.getMessage(), e);
        }
    }


}
