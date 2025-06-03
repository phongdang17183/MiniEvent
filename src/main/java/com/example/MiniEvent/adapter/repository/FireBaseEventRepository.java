package com.example.MiniEvent.adapter.repository;


import com.example.MiniEvent.adapter.web.dto.EventDTO;
import com.example.MiniEvent.model.entity.Event;
import com.example.MiniEvent.model.enums.EventStatus;
import com.example.MiniEvent.model.enums.EventTag;
import com.example.MiniEvent.model.enums.StateType;
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
            ApiFuture<WriteResult> query = firestore.collection("users")
                    .document(event.getCreatedBy())
                    .update("eventCreate", FieldValue.increment(1));
            query.get();

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

    @Override
    public List<Event> findNextPublicEventsFilter(Instant cursorDate, int pageSize, EventTag eventTag) {

        Timestamp firestoreTimestamp = Timestamp.ofTimeSecondsAndNanos(
                cursorDate.getEpochSecond(),
                cursorDate.getNano()
        );

        Query query = firestore.collection("events")
                .whereEqualTo("privateEvent", false)
                .whereEqualTo("eventTag", eventTag.name())
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
            throw new RuntimeException("Firestore query for findNextPublicEvents failed: " + e.getCause());
        }
    }

    @Override
    public boolean isNotCreatedByUserId(String eventId, String userId) {
        Optional<Event> eventOpt = findById(eventId);
        return eventOpt.isEmpty() || !userId.equals(eventOpt.get().getCreatedBy());
    }

    @Override
    public void deleteByUserId(String userId) {
        try {
            ApiFuture<QuerySnapshot> query = firestore.collection("events")
                    .whereEqualTo("createBy", userId)
                    .get();

            WriteBatch batch = firestore.batch();
            List<QueryDocumentSnapshot> documents = query.get().getDocuments();

            for (QueryDocumentSnapshot document : documents) {
                batch.delete(document.getReference());
            }

            batch.commit().get();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to delete events create by user with userId: %s", userId), e);
        }
    }

    @Override
    public List<Event> findByCreateBy(String userId, Instant cursorDate) {
        try {

            Timestamp firestoreTimestamp = Timestamp.ofTimeSecondsAndNanos(
                    cursorDate.getEpochSecond(),
                    cursorDate.getNano()
            );

            Query query = firestore.collection("events")
                    .whereEqualTo("createdBy", userId)
                    .orderBy("date");
                    //.limit(10);

            ApiFuture<QuerySnapshot> future = query.get();

            List<QueryDocumentSnapshot> documentSnapshots = future.get().getDocuments();
            return documentSnapshots.stream()
                    .map(documentSnapshot -> documentSnapshot.toObject(Event.class))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(String.format("Failed to find events create by user with userId: %s", userId), e.getCause());
        }
    }
}
