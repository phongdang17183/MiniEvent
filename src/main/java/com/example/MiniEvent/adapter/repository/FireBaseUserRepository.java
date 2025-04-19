package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.AppUser;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FireBaseUserRepository implements UserRepository{
    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Override
    public AppUser save(AppUser user) {
        try {
            firestore.collection("users").document(user.getId()).set(user).get();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
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

    @Override
    public Optional<AppUser> findByUid(String uid) {
        try {
            DocumentSnapshot document = firestore.collection("users").document(uid).get().get();
            if (!document.exists()) {
                return Optional.empty();
            }

            AppUser user = document.toObject(AppUser.class);
            if (user == null) {
                return Optional.empty();
            }
            return Optional.of(user);

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by uid: " + e.getMessage(), e);
        }
    }
}
