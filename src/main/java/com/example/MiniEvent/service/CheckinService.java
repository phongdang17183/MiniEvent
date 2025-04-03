package com.example.MiniEvent.service;

import com.example.MiniEvent.model.Checkin;
import com.example.MiniEvent.model.CheckinRequest;
import com.example.MiniEvent.model.Event;
import com.example.MiniEvent.response.ResponseObject;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.GeoPoint;
import com.google.firebase.cloud.FirestoreClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckinService {

    @Value("${app.distance.threshold}")
    private Double DISTANCE_THRESHOLD; // 100m tính bằng km

    public ResponseObject checkin(CheckinRequest request) throws Exception {
        Firestore db = FirestoreClient.getFirestore();

        DocumentSnapshot eventDoc = db.collection("events").document(request.getEventId()).get().get();
        if (!eventDoc.exists()) {
            return ResponseObject.builder()
                    .message("Event not exist")
                    .status(400)
                    .data(null)
                    .build();
        }

        Event event = eventDoc.toObject(Event.class);
        assert event != null;
        GeoPoint eventLocation = event.getLocation();
        double distance = calculateDistance(
                eventLocation.getLatitude(), eventLocation.getLongitude(),
                request.getUserLatitude(), request.getUserLongitude()
        );

        if (distance <= DISTANCE_THRESHOLD) {
            String checkinId = request.getUserId() + "_" + request.getEventId();
            db.collection("checkins").document(checkinId).set(
                    Checkin.builder()
                            .eventId(request.getEventId())
                            .userId(request.getUserId())
                            .location(new GeoPoint(request.getUserLatitude(), request.getUserLongitude()))
                            .date(Timestamp.now())
                            .build()
                    );
            return ResponseObject.builder()
                    .message("Checkin successfully")
                    .status(200)
                    .data(event)
                    .build();
        } else {
            return ResponseObject.builder()
                    .message("Checkin fail (distances: " + String.format("%.2f", distance) + " km)")
                    .status(403)
                    .data(null)
                    .build();
        }
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Bán kính Trái Đất (km)
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c; // Khoảng cách tính bằng km
    }
}
