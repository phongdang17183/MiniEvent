package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Registration;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FireBaseRegistraionRepository implements RegistrationRepository {

    private final Firestore firestore;

    @Override
    public Registration save(Registration registration) {
        try {
            ApiFuture<WriteResult> query = firestore.collection("registrations")
                    .document(registration.getId())
                    .set(registration);
            query.get();

            ApiFuture<WriteResult> query1 = firestore.collection("users")
                    .document(registration.getUserId())
                    .update("eventJoin", FieldValue.increment(1));
            query1.get();

            return registration;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save registration: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<Registration> findByUserIdAndEventId(String userId, String eventId) {
        return Optional.empty();
    }
}
