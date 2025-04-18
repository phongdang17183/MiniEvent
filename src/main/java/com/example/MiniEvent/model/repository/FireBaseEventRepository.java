package com.example.MiniEvent.model.repository;


import com.example.MiniEvent.model.entity.Event;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FireBaseEventRepository implements EventRepository{
    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Override
    public Event save(Event event) {
        return null;
    }

    @Override
    public Event findById(String id) {
        return null;
    }
}
