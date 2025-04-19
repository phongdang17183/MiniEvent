package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.AppUser;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
}
