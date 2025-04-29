package com.example.MiniEvent.adapter.repository;


import com.example.MiniEvent.model.entity.Event;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
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

    @Override
    public List<Event> findPublicEvent() {
        try {
            QuerySnapshot snapshot = firestore.collection("events")
                    .whereEqualTo("privateEvent", false)
                    .get()
                    .get();

            List<QueryDocumentSnapshot> documents = snapshot.getDocuments();

            List<Event> events = new ArrayList<>();
            for (QueryDocumentSnapshot doc : documents) {
                Event event = doc.toObject(Event.class);
                events.add(event);
            }

            return events;

        } catch (Exception e) {
            throw new RuntimeException("Firestore query failed", e);
        }
    }
}
