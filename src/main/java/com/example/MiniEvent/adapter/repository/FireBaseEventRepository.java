package com.example.MiniEvent.adapter.repository;


import com.example.MiniEvent.model.entity.Event;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<Event> findNextPublicEvents(Instant cursorDate, int pageSize) {

        Timestamp firestoreTimestamp = Timestamp.ofTimeSecondsAndNanos(
                cursorDate.getEpochSecond(),
                cursorDate.getNano()
        );

        Query query = firestore.collection("events")
                .whereEqualTo("privateEvent", false)
                .whereGreaterThan("date", firestoreTimestamp)
                .orderBy("date")
                .limit(pageSize);
        try {
            ApiFuture<QuerySnapshot> future = query.get();
            QuerySnapshot snapshot = future.get();
            return snapshot.getDocuments()
                    .stream()
                    .map(doc -> doc.toObject(Event.class))
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Firestore query for findNextPublicEvents failed", e);
        }
    }
}
