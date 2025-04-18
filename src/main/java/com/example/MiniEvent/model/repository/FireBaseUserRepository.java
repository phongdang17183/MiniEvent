package com.example.MiniEvent.model.repository;

import com.example.MiniEvent.model.entity.AppUser;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
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
            if (user.getId() == null) {
                UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                        .setEmail(user.getEmail())
                        .setPassword(user.getPassword());
                UserRecord firebaseUser = firebaseAuth.createUser(createRequest);
                user.setId(firebaseUser.getUid());
            }

            firestore.collection("users").document(user.getId()).set(user).get();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    @Override
    public AppUser findById(String id) {
        try {
            return firestore.collection("users").document(id).get().get().toObject(AppUser.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        try {
            return firebaseAuth.getUserByEmail(email) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
