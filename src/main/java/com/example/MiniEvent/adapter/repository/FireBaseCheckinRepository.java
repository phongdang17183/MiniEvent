package com.example.MiniEvent.adapter.repository;

import com.example.MiniEvent.model.entity.Checkin;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FireBaseCheckinRepository implements CheckinRepository{

    private final Firestore firestore;
    private final FirebaseAuth firebaseAuth;

    @Override
    public Checkin save(Checkin checkin) {
        try {
            ApiFuture<WriteResult> query = firestore.collection("checkins")
                    .document(checkin.getId())
                    .set(checkin);
            query.get();
            return checkin;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save checkin: " + e.getMessage(), e);
        }
    }
}
