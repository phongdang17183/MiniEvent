package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.AppUser;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.AuthErrorCode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<AppUser> findAllByPhoneAfter(String phone, Instant cursor) {
        try {
            Timestamp firestoreTimestamp = Timestamp.ofTimeSecondsAndNanos(
                    cursor.getEpochSecond(),
                    cursor.getNano()
            );


            Query query = firestore.collection("users")
                    .whereEqualTo("phone", phone)
                    .orderBy("createDay")
                    .limit(10)
                    .startAfter(firestoreTimestamp);

            ApiFuture<QuerySnapshot> future = query.get();

            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            return documents.stream()
                    .map(doc -> doc.toObject(AppUser.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by phone number: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<AppUser> findByPhone(String phone) {
        try {
            Query query = firestore.collection("users")
                    .whereEqualTo("phone", phone)
                    .limit(1);

            ApiFuture<QuerySnapshot> future = query.get();
            List<AppUser> users = future.get().toObjects(AppUser.class);

            if (users.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(users.getFirst());
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user by phone number: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteByUserId(String userId) {
        try {
            ApiFuture<WriteResult> query = firestore.collection("users")
                    .document(userId)
                    .delete();

            query.get();
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete registration with userId", e);
        }
    }


}
