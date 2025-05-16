package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.adapter.web.exception.DataNotFoundException;
import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.entity.Registration;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        try {
            Query query = firestore.collection("registrations")
                    .whereEqualTo("userId", userId)
                    .whereEqualTo("eventId", eventId)
                    .limit(1);

            ApiFuture<QuerySnapshot> future = query.get();
            List<Registration> registrations = future.get().toObjects(Registration.class);

            if (registrations.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(registrations.getFirst());
        } catch (Exception e) {
            throw new DataNotFoundException("Failed to find registration: " + e.getMessage(), HttpStatus.NOT_FOUND, e);
        }
    }

    @Override
    public Registration delete(Registration registration) {
        try {
            ApiFuture<WriteResult> future = firestore.collection("registrations")
                    .document(registration.getId())
                    .delete();

            future.get();
            return registration;
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete registration: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Registration> findByEventId(String eventId) {
        try {
            ApiFuture<QuerySnapshot> query = firestore.collection("registrations")
                    .whereEqualTo("eventId", eventId)
                    .get();

            List<QueryDocumentSnapshot> documents = query.get().getDocuments();

            return documents.stream()
                    .map(doc -> doc.toObject(Registration.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Firestore query failed", e);
        }
    }

}
