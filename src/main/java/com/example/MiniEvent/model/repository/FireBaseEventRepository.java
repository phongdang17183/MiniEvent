package com.example.MiniEvent.model.repository;


import com.example.MiniEvent.model.entity.AppUser;
import com.example.MiniEvent.model.entity.Event;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FireBaseEventRepository implements EventRepository{
    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Override
    public Event save(Event event) {
        try {
            firestore.collection("events").document(event.getId()).set(event).get();
            return event;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save event: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Event> findById(String id) {
        try {
            DocumentSnapshot snapshot = firestore.collection("events").document(id).get().get();
            if (!snapshot.exists()) {
                return Optional.empty();
            }
            return Optional.ofNullable(snapshot.toObject(Event.class));
        } catch (Exception e) {
            throw new RuntimeException("Firestore query failed", e);
        }
    }
}
